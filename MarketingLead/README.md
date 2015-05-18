SAP Cloud for Customer Integration with facebook using SAP HANA Cloud Platform for Marketing Lead Creation
=============

## Prerequisites:

1.	Eclipse Kepler IDE installed with SAP HANA Cloud Platform Tools plugins
2.	JDK 1.7 is available as an Installed JRE in *Windows->Preferences->Java->Installed JREs*
3.	SAP HANA Cloud JAVA EE6 Web profile is available as a runtime environment *Windows->Preferences->Server-> Runtime Environments*
4.	A Facebook application is available of type APP for Pages and the required app access token and page access token are available.
5.	You have access to a SAP Cloud for Customer tenant.

## What is it about?

This project integrates facebook with SAP Cloud for Customer for creation of marketing lead based on user likes for a specific promotion.

A promotion is created as a facebook post in a page to which a facebook app is bound. When a user who has subscribed to this app likes the Facebook post, the user information is captured from Facebook via real time subscription and a marketing lead is created in the SAP Cloud for Customer through a web service call.

## How to deploy the application?

Step 1: Clone the Git repository

Step 2: Import the project as a Maven project into your eclipse workspace. 
*Note - Make sure the project uses JDK 1.7. This can be configured in the project build path*

Step 3: Run Maven goal clean install 

Step 4: In the local.properties file (*\\PromotionalCompanyApp\WebContent\WEB-INF\local.properties*)adapt all the values:

- VERIFY_TOKEN         (facebook APP verification token)
- APP_ACCESS_TOKEN     (facebook APP access token)
- PAGE_ACCESS_TOKEN    (facebook Page access token)
- USERNAME             (SAP Cloud for Customer User)
- PASSWORD             (SAP Cloud for Customer Password)
- SERVICE_URL		   (SAP Cloud for Customer Marketing lead End point URL)- example:https://<host>/sap/bc/srt/scs/sap/managemarketingleadin



Step 5: Build and deploy your application. **Please note you need to use the SAP HANA Cloud JAVA EE6 Web profile as the runtime enviornment**

Step 6: In the facebook app subscribe to the HANA Cloud Platform APP ConsumerAPP servlet (URL pattern /FBCallBackApp/) in the real time subscription

Authors:
-------
**Abinash Nanda**

+https://twitter.com/abinashnanda

Copyright and license
-------
Copyright 2015 SAP AG

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this work except in compliance with the License. You may obtain a copy of the License in the LICENSE file, or at:

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.

Find the project description at documents/index.html

