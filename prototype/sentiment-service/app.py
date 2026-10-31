from contextlib import asynccontextmanager

from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
from transformers import pipeline
from pydantic import BaseModel

MODEL_NAME = "cardiffnlp/twitter-xlm-roberta-base-sentiment"

# Modell beim Start des Dienstes einmal laden
@asynccontextmanager
async def lifespan(app: FastAPI):
    print(f"Lade Sentiment-Modell: {MODEL_NAME}")
    app.state.sentiment_model = pipeline(
        "text-classification",
        model=MODEL_NAME,
        tokenizer=MODEL_NAME,
        device=-1,
    )
    print("Sentiment-Modell erfolgreich geladen.")
    yield


app = FastAPI(lifespan=lifespan)

# Format der eingehenden Anfrage
class SentimentRequest(BaseModel):
    text: str

# HTTP-Endpunkt für die Sentimentanalyse
@app.post("/sentiment")
def analyze_sentiment(request: SentimentRequest):
    if not request.text.strip():
        raise HTTPException(
            status_code=400,
            detail="Der Kommentartext darf nicht leer sein.",
        )

    result = app.state.sentiment_model(
        request.text,
        truncation=True,
        max_length=512
    )[0]

    return {
        "sentiment": result["label"].lower(),
        "confidence": round(float(result["score"]), 4),
    }

# Test-Endpunkt zur Prüfung, ob der Dienst läuft
@app.get("/health")
def health():
    return {"status": "ok"}