##WebAPI docs##

**Get languages list**

- *URL:* `/api/languages`
- *Method:* `GET`
- *Description:* List of all supported ISO639-1 languages
- *Input:* `none`
- *Output:*

        [
        {
            "code": "be",
            "name": "Belarusian"
        },
        {
            "code": "as",
            "name": "Assamese"
        },
        ...
        ]


**Get current survey**

- *URL:* `/api/survey`
- *Method:* `GET`
- *Headers:* `token:{apitoken}`
- *Description:* Get current survey for selected device and selected language, if translation not exist - will be returned default language
- *Input:* `?devicetoken={x}&language={y}`
- *Output:*

        {
        "surveyId":1,
        "features":
            [
                {
                "featureId":2,
                "featureText":"Feature 2 en-text"
                },
                {
                "featureId":3,
                "featureText":"Feature 3 en-text"
                },
                ...
            ]
        }


**Vote**

- *URL:* `/api/vote`
- *Method:* `POST`
- *Headers:*
        token:{apitoken}
        Content-Type:application/json
- *Description:* Vote for some feature in selected survey
- *Input:*

        {
        "deviceToken":"device-token-1",
        "surveyId":1,
        "featureId":1,
        "language":"en"
        }

- *Output:*

        {
        "deviceToken": "device-token-1",
        "surveyId": 2,
        "featureId": 4,
        "language": "en",
        "auditTimestamps":{
                "createdAt": "2014-12-11T14:00:38.696+0400",
                "modifiedAt": "2014-12-11T14:00:38.696+0400"
            }
        }
