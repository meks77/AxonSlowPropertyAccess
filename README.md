# AxonSlowPropertyAccess

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: <https://quarkus.io/>.

The aim of this project is, to provide an example, where you can see that

* the accessor of same property of the same class is searched very often
* accessors for properties are searched, where this classes do not contain this properties

There are 2 things which confuse me:
- As I understood, there should be a cache, which should search just once per property and per class for the accessor, but in the logs, I can see a different  behaviour. Did I do something wrong?
- Why does the framework try to access properties, which do not exist? Why is the result, that no accessor exists, not cached? Did I do something wrong?

## How to reproduce

### Run the axon server
Run a local axon server with default ports.

### Running the application in dev mode

You shouldn't have a process listening to port 8080.

Run the quarkus process with the help of Intellij or

you can run your application in dev mode that enables live coding using:

```shell script
./gradlew quarkusDev
```

Then, in the console enter "l" and "enter", to avoid live reloading. It doesn't work in this project. If you change something in the code and want to retry, sadly you have to restart.

### Run the test case
Either open [testCase.http](src/test/resources/testCase.http) and execute the post request or do it something else with your prefered tool.
In this test case 21 events are applied. The event handlers are executed and the aggregate is in the expected state.

### Verify the logs in the console out
In the logs I can see 1,247 entries, that methods were not found.
So I would expect, if we apply ~ 1 million events ~ 59 million exceptions are thrown before version 4.10.1+, because the methods do not exist.
