# AxonSlowPropertyAccess

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: <https://quarkus.io/>.

The aim of this project is, to provide an example, where you can see that

* the accessor of same property of the same class is searched very often
* accessors for properties are searched, where this classes do not contain this properties

There are 2 things which confuse me:

- As I understood, there should be a cache, which should search just once per property and per class for the accessor,
  but in the logs, I can see a different behaviour. Did I do something wrong?
- Why does the framework try to access properties, which do not exist? Why is the result, that no accessor exists, not
  cached? Did I do something wrong?

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

Then, in the console enter "l" and "enter", to avoid live reloading. It doesn't work in this project. If you change
something in the code and want to retry, sadly you have to restart.

### Run the test case

Either open [testCase.http](src/test/resources/testCase.http) and execute the post request or do it something else with
your prefered tool.
Invoke the first request of the http file 

### Verify the logs in the console out

In the logs I can see 1,247 entries, that methods were not found.
So I would expect, if we apply ~ 1 million events ~ 59 million exceptions are thrown before version 4.10.1+, because the
methods do not exist.

### Activate my cache
If you active my cache for property access, the logs are nearly gone.
In the http file you can see the difference between cache and no cache solution. The more entities exist in an aggregate,
to bigger is the advantage of the cache, because the more often properties are accessed.

To activate the cache you have to build and install https://github.com/meks77/AxonFramework/tree/cputime-decrease-on-property-access
to your local maven repository and modify the file [build.gradle.kts](build.gradle.kts)

Finally, this simple cache helps to decrease the time for the requests, dependending on the number of member instances, by ~ 50 - 66 %. Depending on your model implementation it can be less or more.

```kotlin
//    implementation("org.axonframework:axon-messaging")
    implementation("org.axonframework:axon-messaging:4.10.1-SNAPSHOT-meks77")
```
Then restart the application and retry the test cases.

## Questions

### Why if using 3 time more events and commands, is it 6 times slower without cache? Why if using 3 time more events and commands, is it 4.4 times slower with cache?

The reason is that for each event and for all members of the aggregate, the event is tried to be applied. Because the
member instances are increased, it's getting slower the more entities exist in the hiearchy of the RootAggregate.

| HTTP Request Nr. | Number of Events | Entities/Aggregate | Duration ms |
|-----------------:|-----------------:|-------------------:|------------:|
|                2 |           11,100 |                100 |       8,446 |
|                4 |           33,300 |                100 |      24,711 | 
|                3 |           32,100 |                300 |      37,279 |
|                5 |           25,550 |                500 |      83,130 |

Be careful how you design the model. A big hierarchy can slow down the system extremly.

#### Ideas for solutions
A meta model or a cache of responsible EventSourcingHandler for types of events could maybe improve that.

E.g. Currently at each entity instance it is tried to apply the event, even if this entity doesn't have a handler.

That's just what came in my mind. I have to verify, if this is possible. 