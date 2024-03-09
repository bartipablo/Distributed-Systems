console.log("Starting...");


function disableButtons() {
    var button1 = document.getElementById("submit1");
    var button2 = document.getElementById("submit2");

    button1.disabled = true;
    button1.style.backgroundColor = "grey";
    button1.style.color = "black";
    button1.style.cursor = "default";

    button2.disabled = true;
    button2.style.backgroundColor = "grey";
    button2.style.color = "black";
    button2.style.cursor = "default";
}


function enableButtons() {
    var button1 = document.getElementById("submit1");
    var button2 = document.getElementById("submit2");

    button1.disabled = false;
    button1.style.backgroundColor = "#4CAF50";
    button1.style.color = "white";
    button1.style.cursor = "pointer";

    button2.disabled = false;
    button2.style.backgroundColor = "#4CAF50";
    button2.style.color = "white";
    button2.style.cursor = "pointer";
}


function elementFromHtml(html) {
    var template = document.createElement('template');
    template.innerHTML = html;
    return template.content.firstChild;
}



function currentWeatherDiffApi() {
    disableButtons();

    var status = document.getElementById("status");
    status.innerHTML = "Loading...";

    var cityName = document.getElementById("form1").value;
    if (cityName == "" || cityName == null) {
        status.innerHTML = "Please enter a city name.";
        enableButtons();
        return;
    }

    const controller = new AbortController();
    const signal = controller.signal;
    const fetchPromise = fetch('http://localhost:8080/current-weather-diff-api/' + cityName, { signal });

    // Timeout after 5 seconds
    const timeoutId = setTimeout(() => {
        controller.abort();
        console.log('Fetch request timed out');
        status.innerHTML = "Request timed out.";
    }, 5000);

    fetchPromise
        .then(response => {
            if (!response.ok) {
                throw new Error(response.status);
            }
            return response.json();
        })
        .then(data => {
            displayWeatherDiffApi(data);
            status.innerHTML = "";
        })
        .catch(error => {
            console.error('Fetch error:', error);
            status.innerHTML = "Error fetching data: + " + error;
        })
        .finally(() => {
            clearTimeout(timeoutId); 
            enableButtons();
        });
}


function displayWeatherDiffApi(data) {
    var content = document.getElementById("content");
    content.innerHTML = "Loading... \n elo " + data;
} 