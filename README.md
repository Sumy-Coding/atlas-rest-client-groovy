# confluence-rest-service-groovy

The REST service to operate in bulk on remote Confluence Server / Data Center content by providing Confluence URL, login data and content data.

### Examples:

```groovy
String confluenceUrl = http://localhost:8110"
def token = TokenService.getToken("admin", "admin")
```

using ENV vars:
```groovy
def token = TokenService.getToken(System.getenv("CONF_USER"), System.getenv("CONF_PASS"))
```


Create Page

```groovy
def pageId = 11111
def spaceKey = "DEMO"
pageService.createPage(URL, localTOKEN, spaceKey, pageId, "new title", "body")
```

Create X PAGES

```groovy
for (i in 1..49) {
    def pageBody = RandomGen.getRandomString(20)
    def title = "Groovy Page 2 ${i}"
    println(pageService.createPage(
            local810CONF_URL, localTOKEN,
            "DEMO",
            "pageId", title, pageBody).body)
}
```

Create X Spaces

```groovy
def CONF_URL = "http://localhost:8100"
def num = 100
for (i in 1..<num) {
    println(SpaceService.createSpace(CONF_URL, localTOKEN, "dev${i}", "dev${i}"))
}
```
MOVE page
```groovy
println(pageService.movePage(CONF_URL, TOKEN, 1966087, NEW_ROOT_ID))
```

COPY page
```groovy
println(pageService.copyPage(CONF_URL, TOKEN, 65603, 1966081, "ababa", true, false, false))
```
