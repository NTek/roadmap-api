##Frontend API##

API's divided into private(require authorization) and public(available without authorization)

###Public API's###

**Registration**

- *URL:* `/api/register`
- *Method:* `POST`
- *Content-type:* `application-json`
- *Description:* Public API for registration new users
- *Input:*

        {
            "email": "example@email.com",
            "password": "Example123"
        }
- *Success output (HTTP status 200):* `(empty)`
- *Validation error output (HTTP status 422):*

        {
            "email": "email validation message",
            "password": "password validation message"
        }

**Login**

- *URL:* `/login`
- *Method:* `POST`
- *Content-type:* `application/x-www-form-urlencoded`
- *Description:* Public API for authorization, authorized sessionId in response cookies
- *Input:*

        username:testuser1@mail.com
        password:123
- *Success output (HTTP status 200):*

        {
            "id":100500,
            "email":"user@email.com"
        }

- *Error output (HTTP status 403):* `(empty)`

**Logout**

- *URL:* `/logout`
- *Method:* `GET`
- *Description:* Public API for logout, forcibly removes authenticated session from cookies
- *Input:*

        username:testuser1@mail.com
        password:123
- *Output (HTTP status 200):* `(empty)`

###Private API's###

**Get applications list with inner structure**

- *URL:* `/apps`
- *Method:* `GET`
- *Description:* Getting own applications with all entities
- *Input:* `(empty)`

- *Output:*

        [
            {
                "id": 1,
                "uuid": "dg2dvf2vhdtb34wecf13xv24oim09qbrn2",
                "apiToken": "token1",
                "name": "App1",
                "description": "Description1",
                "activeSurveyId": 1,
                "auditTimestamps": {
                    "createdAt": "2014-12-16T18:24:06.000+0400",
                    "modifiedAt": "2014-12-16T18:24:06.000+0400"
                },
                "applicationUsers": [
                    {
                        "userId": 1,
                        "applicationId": 1,
                        "accessLevel": 0,
                        "auditTimestamps": {
                            "createdAt": "2014-12-16T18:24:06.000+0400",
                            "modifiedAt": "2014-12-16T18:24:06.000+0400"
                        }
                    },
                    ...
                ],
                "applicationFeatures": [
                    {
                        "id": 1,
                        "applicationId": 1,
                        "implemented": false,
                        "auditTimestamps": {
                            "createdAt": "2014-12-16T18:24:06.000+0400",
                            "modifiedAt": "2014-12-16T18:24:06.000+0400"
                        },
                        "localizedFeatures": [
                            {
                                "featureId": 1,
                                "language": "en",
                                "text": "Feature 1 en-text",
                                "auditTimestamps": {
                                    "createdAt": "2014-12-16T18:24:06.000+0400",
                                    "modifiedAt": "2014-12-16T18:24:06.000+0400"
                                }
                            },
                            ...
                        ]
                    },
                    ...
                ],
                "applicationSurveys": [
                    {
                        "id": 1,
                        "uuid": null,
                        "applicationId": 1,
                        "title": "Survey1",
                        "disabled": false,
                        "requiredVotes": 1000,
                        "requiredDate": null,
                        "startedAt": null,
                        "finishedAt": null,
                        "auditTimestamps": {
                            "createdAt": "2014-12-16T18:24:06.000+0400",
                            "modifiedAt": "2014-12-16T18:24:06.000+0400"
                        },
                        "feature": [
                            {
                                "id": 1,
                                "applicationId": 1,
                                "implemented": false,
                                "auditTimestamps": {
                                    "createdAt": "2014-12-16T18:24:06.000+0400",
                                    "modifiedAt": "2014-12-16T18:24:06.000+0400"
                                },
                                "localizedFeatures": [
                                    {
                                        "featureId": 1,
                                        "language": "en",
                                        "text": "Feature 1 en-text",
                                        "auditTimestamps": {
                                            "createdAt": "2014-12-16T18:24:06.000+0400",
                                            "modifiedAt": "2014-12-16T18:24:06.000+0400"
                                        }
                                    },
                                    ...
                                ]
                            },
                            ...
                        ]
                    },
                    ...
                ]
            },
            ...
        ]

**Create new application**

- *URL:* `/apps`
- *Method:* `POST`
- *Description:* Create new application
- *Input:*

            {
                "name": "App1",
                "description": "Description1",
                "applicationFeatures": [
                    {
                        "implemented": false,
                        "localizedFeatures": [
                            {
                                "language": "en",
                                "text": "Feature 1 en-text",
                            },
                            ...
                        ]
                    },
                    ...
                ],
            },

- *Success output (HTTP status 200):* full structure of app, completely similar to one application structure from application list request

- *Validation error output (HTTP status 422):* similar to input but with error message in values


**Edit existed application**

- *URL:* `/apps/{id}`
- *Method:* `PUT`
- *Description:* Edit existed application
- *Input:*

            {
                "name": "App edited name",
                "apiToken": "some random string means that token must be regenerated",
                "description": "Edited description",
                "applicationFeatures": [
                    {
                        "applicationId": 1,
                        "implemented": false,
                        "localizedFeatures": [
                            {
                                "language": "en",
                                "text": "Feature 1 en-text translation",
                            },
                            {
                                "language": "ru",
                                "text": "new translation",
                            },
                            ...
                        ]
                    },
                    ...
                ],
            },

- *Success output (HTTP status 200):* full structure of app, completely similar to one application structure from application list request

- *Validation error output (HTTP status 422):* similar to input but with error message in values


**Create new survey for app**

- *URL:* `/survey`
- *Method:* `POST`
- *Description:* Creates a new survey
- *Input:*

            {
                "applicationId": 1,
                "title":"New survey title",
                "features":[1,2],
                "requiredVotes":300,
                "requiredDate": "2014-12-16T18:24:06.000+0400"
            }

- *Success output (HTTP status 200):*

            {
            "id": 5,
            "uuid": "1e424c9e-1406-4a87-b331-731f1965ad93",
            "applicationId": 1,
            "title": "new survey",
            "disabled": false,
            "requiredVotes": 300,
            "requiredDate": null,
            "startedAt": "2015-01-20T16:38:42.334+0400",
            "finishedAt": "2014-12-16T18:24:06.000+0400",
            "auditTimestamps": {
                "createdAt": "2015-01-20T16:38:42.337+0400",
                "modifiedAt": "2015-01-20T16:38:42.337+0400"
            },
            "feature": [ … ]
            }

- *Validation error output (HTTP status 422):* Similar to success output but with error message in values


**Close active survey in app**

- *URL:* `/survey/{id}/close`
- *Method:* `GET`
- *Description:* Close active survey in application
- *Input:* `(empty)`
- *Output (HTTP status 200):* Survey entity

**Disable/enable active survey in app**

- *URL:* `/survey/{id}/disable` and `/survey/{id}/enable`
- *Method:* `GET`
- *Description:* Disable/enable active survey in application
- *Input:* `(empty)`
- *Output (HTTP status 200):* Survey entity


**Rename active survey in app**

- *URL:* `/survey/{id}/rename`
- *Method:* `GET`
- *Description:* Disable/enable active survey in application
- *Input:* `name=NewName`
- *Output:* Survey entity with changed name

**Delete active survey in app**

- *URL:* `/survey/{id}`
- *Method:* `DELETE`
- *Description:* Delete survey
- *Input:* `none`
- *Output:* `none`
