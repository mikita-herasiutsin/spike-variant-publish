## Sample project to show the issue while using published artifact

It's close but simplified example of what we have in our project.
The idea is to publish generated OpenApi artifact and then use it in another project.
The issue is that generated artifact in maven repository `.m2` (both local and artifactory) doesn't have `files[]` section in `.module` file (`.m2\repository\org\spike\client-app-v1-client\1.0-SNAPSHOT\client-app-v1-client-1.0-SNAPSHOT.module`) in published artifact.
Artifact is successfully checked by Gradle during build, but it is absent on the classpath as jar location is missing in `.module` file.

### Reproducing steps
* `./gradlew publishToMavenLocal`. OpenApi will generate jar from `client-app\src\main\resources\api.yml` and then publish it locally
* `./gradlew build`. Build fails as it can't reference class from generated jar `client-app\src\main\kotlin\org\spike\TestAppKt.kt`
* It can be fixed just by disabling the generation of `.module` file `buildSrc\src\main\kotlin\com\spike\publish\Utils.kt:49`