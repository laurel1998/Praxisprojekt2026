// Backend-Aufrufe in Service-Worker ausgelagert:

chrome.runtime.onMessage.addListener((message, sender, sendResponse) => {
    if (!sender.url?.startsWith("https://www.youtube.com/")) {
        return;
    }

    if (message.type !== "FETCH_COMMENTS" &&
        message.type !== "FETCH_COMMENT" &&
        message.type !== "FETCH_RISK_SCORE"
    ) {
        return;
    }

    let endpoint;

    switch (message.type) {
        case "FETCH_COMMENTS":
            endpoint = "/api/youtube/comments";
            break;

        case "FETCH_COMMENT":
            endpoint = "/api/youtube/comment";
            break;

        case "FETCH_RISK_SCORE":
            endpoint = "/api/risk-score";
            break;
    }

    const url = new URL(endpoint, "http://localhost:8080");

    url.searchParams.set("videoId", message.videoId);

    if (message.type === "FETCH_COMMENT") {
        url.searchParams.set("commentId", message.commentId);
        url.searchParams.set("domParentId", message.domParentId ?? "");

    }

    if (message.type === "FETCH_RISK_SCORE") {
        url.searchParams.set("channelId", message.channelId);
        url.searchParams.set("commentId", message.commentId);

        if (message.domParentId) {
            url.searchParams.set("domParentId", message.domParentId);
        }
    }

    fetch(url)
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            sendResponse({ ok: true, data });
        })
        .catch(error => {
            console.error("Backend-Aufruf fehlgeschlagen:", error);
            sendResponse({ ok: false, error: error.message });
        });

    return true;
});
