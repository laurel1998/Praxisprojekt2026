console.log("Detection: Content Script läuft!");

function getVideoId() {
    return new URLSearchParams(window.location.search).get("v");
}

function getCommentId(commentElement) {
    const link = commentElement.querySelector(
        '#published-time-text a[href*="lc="]'
    );

    if (!link) {
        return null;
    }

    const url = new URL(link.href);
    return url.searchParams.get("lc");
}

function sendCommentToBackend(videoId, commentId) {
    fetch(
        `http://localhost:8080/api/youtube/comments?videoId=${encodeURIComponent(videoId)}`
    )
        .then(response => response.json())
        .then(data => {
            console.log("Backend-Antwort:", data);
        })
        .catch(error => {
            console.error("Fehler beim Backend-Aufruf:", error);
        });

    fetch(
        `http://localhost:8080/api/youtube/comment?videoId=${encodeURIComponent(videoId)}&commentId=${encodeURIComponent(commentId)}`
    )
        .then(response => response.json())
        .then(data => {
            console.log("Gesendet:", commentId);
            console.log("Erhalten:", data.id);
            
            console.log("IDs gleich:", commentId === data.id);

            console.log("Kommentar:", data);
        })
        .catch(error => {
            console.error("Fehler beim Laden des Kommentars:", error);
        });
}

function observeCommentSection(commentSection) {
    const processedComments = new Set();

    function processComments() {
        const comments = commentSection.querySelectorAll(
                "ytd-comment-thread-renderer"
        );

        console.log("Gefundene Kommentare:", comments.length);

        comments.forEach(comment => {
            const commentId = getCommentId(comment);
            if (!commentId) {
                console.log("Kommentar gefunden, aber noch keine Kommentar-ID");

                return;
            }

            if (processedComments.has(commentId)) {
                return;
            }
            processedComments.add(commentId);

            const videoId = getVideoId();

            console.log("Video-ID:", videoId);
            console.log("Kommentar-ID:", commentId);

            sendCommentToBackend(videoId, commentId);
        });
    }

    processComments();

    const observer = new MutationObserver(() => {
        processComments();
    });

    observer.observe(commentSection, {
        childList: true,
        subtree: true
    });
}

const domObserver = new MutationObserver(() => {
    const commentSection = document.querySelector("ytd-comments#comments");

    if (commentSection) {
        console.log("Kommentarbereich wurde gefunden!");

        domObserver.disconnect();
        observeCommentSection(commentSection);
    }
});

domObserver.observe(document.body, {
    childList: true,
    subtree: true
});