async function uploadPdf() {

    const fileInput = document.getElementById("pdfFile");
    const status = document.getElementById("uploadStatus");

    const file = fileInput.files[0];

    if (!file) {
        alert("Please select a PDF file");
        return;
    }

    status.innerText = "Uploading and processing PDF...";

    const formData = new FormData();
    formData.append("file", file);

    try {

        const response = await fetch("/pdf/upload", {
            method: "POST",
            body: formData
        });

        const data = await response.json();

        localStorage.setItem("sessionId", data.sessionId);

        status.innerText = "PDF processed successfully ✅";

    } catch (error) {

        status.innerText = "Error uploading PDF ❌";

    }
}


async function askQuestion(){

    const question = document.getElementById("question").value;
    const answerBox = document.getElementById("answer");
    const loading = document.getElementById("loadingStatus");

    const sessionId = localStorage.getItem("sessionId");

    if(!sessionId){
        alert("Please upload a PDF first.");
        return;
    }

    if(!question){
        alert("Please enter a question.");
        return;
    }

    loading.innerText = "Getting answer...";

    answerBox.innerText = "";

    try {

        const response = await fetch(
            `/chat?sessionId=${sessionId}&question=${encodeURIComponent(question)}`
        );

        const answer = await response.text();

        loading.innerText = "";

        answerBox.innerText = answer;

    } catch (error){

        loading.innerText = "";
        answerBox.innerText = "Error getting answer.";

    }
}
