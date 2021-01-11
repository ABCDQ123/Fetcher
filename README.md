#     Fetcher
#     local image or video loader

```
Fetcher.putFilter(FetcherMIME.GIF);
Fetcher.start(MainActivity.this, getBaseContext(), FetcherKey.MEDIA_ALL, MainActivity.this);
```
```
Fetcher.destroy();
```
```
String mime = (String) object.opt(FetcherKey.mime());
String name = (String) object.opt(FetcherKey.name());
String path = (String) object.opt(FetcherKey.path());
long duration = (long) object.opt(FetcherKey.duration());
```
