
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


function getApiKey() {
    var apiKey = document.getElementById("apiKey").value;
    if (apiKey == null) {
        return "";
    } 
    return apiKey;
}


/*
    differences between weather forecasts for different APIs.
*/
function currentWeatherDiffApi() {
    disableButtons();

    var status = document.getElementById("status");
    var content = document.getElementById("content");
    status.innerHTML = "Loading...";

    var cityName = document.getElementById("form1").value;
    if (cityName == "" || cityName == null) {
        status.innerHTML = "Please enter a city name.";
        enableButtons();
        return;
    }

    const apiKey = getApiKey();

    const controller = new AbortController();
    const signal = controller.signal;
    const fetchPromise = fetch('http://localhost:8080/current-weather-diff-api/' + cityName + "?key=" + apiKey, { signal });

    const timeoutId = setTimeout(() => {
        controller.abort();
        console.log('Fetch request timed out');
        status.innerHTML = "Request timed out.";
    }, 5000);

    fetchPromise
        .then(response => {
            if (!response.ok) {
                return response.text().then(text => { throw new Error(response.status + " " + text) })
            }
            return response.json();
        })
        .then(data => {
            displayWeatherDiffApi(data);
            status.innerHTML = "";
        })
        .catch(error => {
            console.error('Error:', error.message);
            status.innerHTML = "Error: " + error.message;
            content.innerHTML = "";
        })
        .finally(() => {
            clearTimeout(timeoutId); 
            enableButtons();
        });
}


function displayWeatherDiffApi(data) {
    var content = document.getElementById("content");

    const location = data.location;
    const locationDiv = `
    <div>
        <h2>Location</h2>
        Name: ${location.name} <br />
        Country: ${location.country} <br />
        Lititude: ${location.latitude} <br />
        Longitude: ${location.longitude}
    </div>`;

    const weatherForecastByApiA = data.weatherForecastByApiA;
    const weatherForecastADiv = `
    <div>
        <h2>Weather Forecast 1</h2>
        temperature: ${weatherForecastByApiA.temperature} <br />
        wind speed: ${weatherForecastByApiA.windSpeed} <br />
        cloud cover: ${weatherForecastByApiA.cloudCover} <br />
        wind direction: ${weatherForecastByApiA.windDirection} <br />
        summary: ${weatherForecastByApiA.summary} <br />
        api source: ${weatherForecastByApiA.apiSource}
    </div>`;

    const weatherForecastByApiB = data.weatherForecastByApiB;
    const weatherForecastBDiv = `
    <div>
        <h2>Weather Forecast 2</h2>
        temperature: ${weatherForecastByApiB.temperature} <br />
        wind speed: ${weatherForecastByApiB.windSpeed} <br />
        cloud cover: ${weatherForecastByApiB.cloudCover} <br />
        wind direction: ${weatherForecastByApiB.windDirection} <br />
        summary: ${weatherForecastByApiB.summary} <br />
        api source: ${weatherForecastByApiB.apiSource}
    </div>`;


    const differenceDiv = `
    <div>
        <h2>Difference</h2>
        Average temperature: ${data.avgTemperature} <br />
        Absolute temperature difference: ${data.absTemperatureDifference} <br />
        Relative temperature difference: ${data.relTemperatureDifference} <br />
        <br />
        Average wind speed: ${data.avgWindSpeed} <br />
        Absolute wind speed difference: ${data.absWindSpeedDifference} <br />
        Relative wind speed difference: ${data.relWindSpeedDifference} <br />
        <br />
        Average cloud cover: ${data.avgCloudCover} <br />
        Absolute cloud cover: ${data.absCloudCoverDifference} <br />
        Relative cloud cover difference: ${data.relCloudCoverDifference} <br />
        <br />
    </div>`;

    const contentHTML = `
    <div id="content" class="content-container">
        ${locationDiv}
        ${weatherForecastADiv}
        ${weatherForecastBDiv}
        ${differenceDiv}
    </div>`;

    content.innerHTML = contentHTML;
} 




/*
    differences between weather forecasts for different cities.
*/
function currentWeatherDiffCity() {
    disableButtons();

    var status = document.getElementById("status");
    var content = document.getElementById("content");
    status.innerHTML = "Loading...";

    var cityName1 = document.getElementById("form2").value;
    var cityName2 = document.getElementById("form3").value;
    if (cityName1 == "" || cityName1 == null || cityName2 == "" || cityName2 == null) {
        status.innerHTML = "Please enter a city name.";
        enableButtons();
        return;
    }

    const apiKey = getApiKey();

    const controller = new AbortController();
    const signal = controller.signal;
    const fetchPromise = fetch('http://localhost:8080/current-weather-diff-cities/?city1=' + cityName1 + "&city2=" + cityName2 + "&key=" + apiKey, { signal });

    const timeoutId = setTimeout(() => {
        controller.abort();
        console.log('Fetch request timed out');
        status.innerHTML = "Request timed out.";
    }, 5000);

    fetchPromise
        .then(response => {
            if (!response.ok) {
                return response.text().then(text => { throw new Error(response.status + " " + text) })
            }
            return response.json();
        })
        .then(data => {
            displayWeatherDiffCity(data);
            status.innerHTML = "";
        })
        .catch(error => {
            console.error('Error:', error.message);
            status.innerHTML = "Error: " + error.message;
            content.innerHTML = "";
        })
        .finally(() => {
            clearTimeout(timeoutId); 
            enableButtons();
        });
}


function displayWeatherDiffCity(data) {
    var content = document.getElementById("content");

    const locationA = data.locationA;
    const locationADiv = `
    <div>
        <h2>Location 1</h2>
        Name: ${locationA.name} <br />
        Country: ${locationA.country} <br />
        Lititude: ${locationA.latitude} <br />
        Longitude: ${locationA.longitude}
    </div>`;

    const locationB = data.locationB;
    const locationBDiv = `
    <div>
        <h2>Location 2</h2>
        Name: ${locationB.name} <br />
        Country: ${locationB.country} <br />
        Lititude: ${locationB.latitude} <br />
        Longitude: ${locationB.longitude}
    </div>`;

    const weatherForecastAtLocationA = data.weatherForecastAtLocationA;
    const weatherForecastAtLocationADiv = `
    <div>
        <h2>Weather Forecast 1</h2>
        temperature: ${weatherForecastAtLocationA.temperature} <br />
        wind speed: ${weatherForecastAtLocationA.windSpeed} <br />
        cloud cover: ${weatherForecastAtLocationA.cloudCover} <br />
        wind direction: ${weatherForecastAtLocationA.windDirection} <br />
        summary: ${weatherForecastAtLocationA.summary} <br />
        api source: ${weatherForecastAtLocationA.apiSource}
    </div>`;

    const weatherForecastAtLocationB = data.weatherForecastAtLocationB;
    const weatherForecastAtLocationBDiv = `
    <div>
        <h2>Weather Forecast 1</h2>
        temperature: ${weatherForecastAtLocationB.temperature} <br />
        wind speed: ${weatherForecastAtLocationB.windSpeed} <br />
        cloud cover: ${weatherForecastAtLocationB.cloudCover} <br />
        wind direction: ${weatherForecastAtLocationB.windDirection} <br />
        summary: ${weatherForecastAtLocationB.summary} <br />
        api source: ${weatherForecastAtLocationB.apiSource}
    </div>`;


    const differenceDiv = `
    <div>
        <h2>Difference</h2>
        Absolute temperature difference: ${data.absTemperatureDifference} <br />
        Relative temperature difference: ${data.relTemperatureDifference} <br />
        <br />
        Absolute wind speed difference: ${data.absWindSpeedDifference} <br />
        Relative wind speed difference: ${data.relWindSpeedDifference} <br />
        <br />
        Absolute cloud cover: ${data.absCloudCoverDifference} <br />
        Relative cloud cover difference: ${data.relCloudCoverDifference} <br />
        <br />
    </div>`;

    const contentHTML = `
    <div id="content" class="content-container">
        ${locationADiv}
        ${weatherForecastAtLocationADiv}
        ${locationBDiv}
        ${weatherForecastAtLocationBDiv}
        ${differenceDiv}
    </div>`;

    content.innerHTML = contentHTML;
} 