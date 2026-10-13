// Backend-Aufrufe in Service-Worker ausgelagert:

chrome.runtime.onMessage.addListener((message, sender, sendResponse) => {
    if (!sender.url?.startsWith("https://www.youtube.com/")) {
        return;
    }

    if (message.type !== "FETCH_COMMENTS" &&
        message.type !== "FETCH_COMMENT") {
        return;
    }

    const url = new URL(
        message.type === "FETCH_COMMENTS"
            ? "/api/youtube/comments"
            : "/api/youtube/comment",
        "http://localhost:8080"
    );

    url.searchParams.set("videoId", message.videoId);

    if (message.type === "FETCH_COMMENT") {
        url.searchParams.set("commentId", message.commentId);
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
