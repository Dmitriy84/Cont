package com.continuum.cucumber.utils;

import com.continuum.cucumber.shell.Shell;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

@Slf4j
class Artifactory {
    static JSONObject getLatestBuildNumberOfRepository() {
        JSONObject repositoryBuildNumbersObject = new JSONObject();

        for (String repositoryName : Shell.Application.getRepoNames()) {
            int buildNumber = getLatestBuildNumberOfRepositorySupplied(repositoryName);
            if (buildNumber != -1)
                repositoryBuildNumbersObject.put(repositoryName, buildNumber);
        }

        log.info("Successfully fetched the latest build numbers for the requested repositories.");
        return repositoryBuildNumbersObject;
    }

    private static int getLatestBuildNumberOfRepositorySupplied(String repositoryName) {
        val buildNumbers = new ArrayList<Integer>();
        if (repositoryName.isEmpty())
            repositoryName = "invalid";

        val response = RestAssuredUtility.getWithNoParameters("http://artifact.corp.continuum.net:8081/artifactory/api/build/" + repositoryName);
        if (response.statusCode() != 200) {
            return -1;
        } else {
            JSONObject responseObject = JsonUtility.convertResponseToJsonObject(response);
            JSONArray buildNumbersArrayObject = (JSONArray) responseObject.get("buildsNumbers");

            int latestBuildNumber;
            for (latestBuildNumber = 0; latestBuildNumber < buildNumbersArrayObject.size(); ++latestBuildNumber) {
                JSONObject buildNumberSpecificObject = (JSONObject) buildNumbersArrayObject.get(latestBuildNumber);
                int buildNumber = Integer.parseInt(buildNumberSpecificObject.get("uri").toString().replace("/", ""));
                buildNumbers.add(buildNumber);
            }

            latestBuildNumber = Collections.max(buildNumbers);
            log.info("Latest build number of repository '" + repositoryName + "' is: " + latestBuildNumber);
            return latestBuildNumber;
        }
    }

    static String formTheBuildVersionsForReporting(JSONObject buildVersionsObject) {
        String formedBuildVersions = "";

        String key;
        for (Iterator iterator = buildVersionsObject.keySet().iterator(); iterator.hasNext();
             formedBuildVersions += " " + key + " : " + buildVersionsObject.get(key)) {
            key = (String) iterator.next();
        }

        return formedBuildVersions;
    }
}