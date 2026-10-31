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

function getDomParentId(commentElement) {
    const parentReplies = commentElement.parentElement
        ?.closest("ytd-comment-replies-renderer");

    const parentComment = parentReplies?.parentElement
        ?.closest("ytd-comment-thread-renderer");

    if (!parentComment) return null;

    return getCommentId(parentComment);
}

function sendCommentToBackend(videoId, commentId, domParentId) {
    
    chrome.runtime.sendMessage({
        type: "FETCH_COMMENTS",
        videoId: videoId
    })
        .then(response => {
            if (!response?.ok) {
                throw new Error(response?.error || "Keine Backend-Antwort");
            }

            console.log("Backend-Antwort:", response.data);
        })
        .catch(error => {
            console.error("Fehler beim Laden der Kommentare:", error);
        });

    chrome.runtime.sendMessage({
        type: "FETCH_COMMENT",
        videoId: videoId,
        commentId: commentId,
        domParentId: domParentId
    })
        .then(response => {
            if (!response?.ok) {
                throw new Error(response?.error || "Keine Backend-Antwort");
            }

            const comment = response.data;

            if (commentId !== comment.id) {
                return;
            }

            return chrome.runtime.sendMessage({
                type: "FETCH_RISK_SCORE",
                videoId: videoId,
                channelId: comment.authorChannelId,
                commentId: commentId,
                domParentId: domParentId
            });
        })
        .then(response => {
            if (!response) return;

            if (!response.ok) {
                throw new Error(response.error || "Risk Score konnte nicht geladen werden");
            }

            const score = response.data.score;

            addRiskPoint(commentId, score);
        })
        .catch(error => {
            console.error("Fehler beim Laden des Kommentars:", error);
        });
}

function getRiskColor(score) {
    if (score < 34) {
        return "green";
    }
    if (score < 67) {
        return "orange";
    }
    return "red";
}

function addRiskPoint(commentId, score) {
    const comments = document.querySelectorAll(
        "ytd-comment-thread-renderer"
    );

    comments.forEach(comment => {
        const currentCommentId = getCommentId(comment);

        if (currentCommentId !== commentId) {
            return;
        }
        if (comment.querySelector(".risk-score-test-point")) {
            return;
        }

        const testPoint = document.createElement("span");

        testPoint.className = "risk-score-test-point";
        testPoint.textContent = "●";
        testPoint.style.color = getRiskColor(score);
        testPoint.style.fontSize = "16px";
        testPoint.style.marginRight = "8px";

        const commentContent = comment.querySelector("#content-text");

        if (commentContent) {
            commentContent.prepend(testPoint);
        }

        console.log(
            "Risk Score visualisiert",
            commentId,
            "Score:",
            score
        );
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
            const domParentId = getDomParentId(comment);

            if (!commentId) {
                console.log("Kommentar gefunden, aber noch keine Kommentar-ID");
                return;
            }

            if (processedComments.has(commentId)) {
                return;
            }
            processedComments.add(commentId);

            const videoId = getVideoId();

            sendCommentToBackend(videoId, commentId, domParentId);
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