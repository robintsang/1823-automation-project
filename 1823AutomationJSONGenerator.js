function uploadJSONToGitHub() {
  var ss = SpreadsheetApp.getActiveSpreadsheet();
  if (!ss) {
    throw new Error("No active spreadsheet. Please run this script from a Google Sheet or via a trigger.");
  }
  var sheet = ss.getActiveSheet();
  var lastRow = sheet.getLastRow();
  var row = sheet.getRange(lastRow, 1, 1, sheet.getLastColumn()).getValues()[0];
  var headers = sheet.getRange(1, 1, 1, sheet.getLastColumn()).getValues()[0];

  // Please manually set the first row (header) of your Google Sheet to clean English keys, e.g.:
  // name, email, phone, category, sub_category_1, sub_category_2, other_sub_category_detail, case_info, location, attachment, timestamp

  var data = {};
  for (var i = 0; i < headers.length; i++) {
    var key = headers[i];
    data[key] = row[i];
  }

  // Support both 'timestamp' and '時間戳記' as the timestamp field
  var timestampKey = null;
  if (headers.indexOf('timestamp') !== -1) {
    timestampKey = 'timestamp';
  } else if (headers.indexOf('時間戳記') !== -1) {
    timestampKey = '時間戳記';
  }
  var timestampValue = timestampKey ? data[timestampKey] : new Date().toISOString();
  // Ensure timestampValue is a string
  var timestampString = timestampValue instanceof Date
    ? timestampValue.toISOString()
    : String(timestampValue || new Date().toISOString());

  // Generate file name: complaint_{category}_{timestamp}_{name}.json
  // Lowercase, replace spaces with underscores, remove non-alphanumeric except underscore and dash
  function clean(str) {
    return String(str || "").toLowerCase().replace(/\s+/g, '_').replace(/[^a-z0-9_-]/g, '');
  }
  var category = clean(data["category"]);
  var name = clean(data["name"]);
  var timestamp = timestampString.replace(/[:.]/g, "-");
  var fileName = "complaint_" + category + "_" + timestamp + "_" + name + ".json";

  var json = JSON.stringify(data, null, 2);

  // Get GitHub token from Script Properties for security
  // Set the token in Apps Script: Project Settings > Script Properties > Add row: key=GITHUB_TOKEN, value=your_token
  var githubToken = PropertiesService.getScriptProperties().getProperty('GITHUB_TOKEN');
  if (!githubToken) {
    throw new Error('GitHub token not set in Script Properties. Please add GITHUB_TOKEN in Project Settings.');
  }

  var repo = "robintsang/1823-automation-project";
  var path = "complaints/" + fileName;
  var apiUrl = "https://api.github.com/repos/" + repo + "/contents/" + path;

  var payload = {
    "message": "Add new form data",
    "content": Utilities.base64Encode(json)
  };

  var options = {
    "method": "put",
    "contentType": "application/json",
    "headers": {
      "Authorization": "token " + githubToken
    },
    "payload": JSON.stringify(payload)
  };

  var response = UrlFetchApp.fetch(apiUrl, options);
  Logger.log(response.getContentText());
}