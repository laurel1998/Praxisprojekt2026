console.log("Detection: Content Script läuft!");

const videoId = new URLSearchParams(window.location.search).get("v");
console.log("Video-ID:", videoId);

function observeCommentSection(commentSection) {
    const observer = new IntersectionObserver((entries) => {
        if (entries[0].isIntersecting) {
            console.log("Kommentarbereich ist sichtbar!");
        }
    });

    observer.observe(commentSection);
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

//websockets als kommunikation?