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
- *Success output (HTTP status 200):* `(empty)`
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
                                "id": 1,
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
                                        "id": 1,
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

**Edit or create new application**

- *URL:* `/apps`
- *Method:* `POST`
- *Description:* Edit application or create the new (if id's not specified)
- *Input:*

            {
                "id": 1,
                "name": "App1",
                "description": "Description1",
                "applicationFeatures": [
                    {
                        "id": 1,
                        "applicationId": 1,
                        "implemented": false,
                        "localizedFeatures": [
                            {
                                "id": 1,
                                "featureId": 1,
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
