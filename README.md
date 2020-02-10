# FirebaseFirestoreAssistant
A Library to implement an Assistant for Firebase Firestore in Android.

## Demo
The Demo app is available [here](https://github.com/Im-Mark42/FirebaseFirestoreAssistant/tree/master/app/src/main/java/com/sach/mark42/firestoredemo) that demonstrates feature of this android library.

## How to?
Create a new project in Firebase console and link that project in to your android app.
#### Add it in your root build.gradle at the end of repositories:

```
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
    }
}
```

#### Add the dependency

```
dependencies {
          implementation 'com.google.firebase:firebase-core:17.2.1'
          implementation 'com.google.firebase:firebase-firestore:21.3.1'
          
          implementation 'com.github.Im-Mark42:FirebaseFirestoreAssistant::1.0.0'
          
          //Kotlin ViewModelScope only for kotlin user
          implementation group: 'androidx.lifecycle', name: 'lifecycle-viewmodel-ktx', version: '2.2.0-rc02'
}
```
## Getting Started

* Create application class called `App.kt` and `setPersistanceEnabled(true)` to enable firestore caching. Register it in your `AndroidManifest` file and implement below methods.

```
class App : Application() {

    companion object {
        var firebaseInstanceInitialized = false
    }

    override fun onCreate() {
        super.onCreate()

        if (!FirebaseApp.getApps(this).isEmpty() && !firebaseInstanceInitialized) {
            firebaseInstanceInitialized = true
            val db = FirebaseFirestore.getInstance()
            val settings = FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build()
            db.firestoreSettings = settings
        }
    }

}
```

```
<application
        android:name=".App"
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        ...
        
</application>
```

**Java User**

Create application class called `App.java` and `setPersistanceEnabled(true)` to enable firebase caching. Register it in your `AndroidManifest` file and implement below methods.

```
public class App extends Application {

    private static Boolean firebaseInstanceInitialized = false;

    @Override
    public void onCreate() {
        super.onCreate();

        if (!FirebaseApp.getApps(this).isEmpty() && !firebaseInstanceInitialized) {
            firebaseInstanceInitialized = true;
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                    .setPersistenceEnabled(true)
                    .build();
            db.setFirestoreSettings(settings);
        }
    }
}
```

```
<application
        android:name=".App"
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        ...
        
</application>
```

* Create a data class. In this case i am creating a data class for user. (I am using firestore annotation `@get` and `@set` for getter and setter method to consume less data.)

```
@Keep
data class User(
    @Exclude @set:Exclude @get:Exclude
    var userPushId: String? = null,

    @get:PropertyName(FIRESTORE_KEY.USER.firstName)
    @set:PropertyName(FIRESTORE_KEY.USER.firstName)
    var firstName: String = "",

    @get:PropertyName(FIRESTORE_KEY.USER.lastName)
    @set:PropertyName(FIRESTORE_KEY.USER.lastName)
    var lastName: String = "",

    @get:PropertyName(FIRESTORE_KEY.USER.email)
    @set:PropertyName(FIRESTORE_KEY.USER.email)
    var email: String = ""
)
```

**Java User**

Create a model class. In this case it should be `User.java`. Add getter and setter method. if you want to add firestore annotation then add `@PropertyName(//name)` to your getter and setter method to consume less data.

```
public class User {

    private String firstName;
    private String lastName;
    private String email;

    //Required an empty constructor for firabase
    public User() {}

    public User(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    @PropertyName(FIRESTORE_KEY.USER.firstName)
    public String getFirstName() {
        return firstName;
    }

    @PropertyName(FIRESTORE_KEY.USER.firstName)
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @PropertyName(FIRESTORE_KEY.USER.lastName)
    public String getLastName() {
        return lastName;
    }

    @PropertyName(FIRESTORE_KEY.USER.lastName)
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @PropertyName(FIRESTORE_KEY.USER.email)
    public String getEmail() {
        return email;
    }

    @PropertyName(FIRESTORE_KEY.USER.email)
    public void setEmail(String email) {
        this.email = email;
    }
}
```

* Create a `object` to store the PropertyName. In this case i am creating a object called `FIRESTORE_KEY.kt`

```
object FIRESTORE_KEY {

    object USER {
        const val firstName = "fN"
        const val lastName = "lN"
        const val email = "e"
    }
}
```

**Java User**

Create a `class` to store the PropertyName. In this case i am creating a class called `FIRESTORE_KEY.java`

```
public class FIRESTORE_KEY {
    public static class USER {
        public static final String firstName = "fN";
        public static final String lastName = "lN";
        public static final String email = "e";
    }
}
```

* Create a Repository class and extend `FirestoreRepo<T>()` class (use your data class in case of T). Override `convertDocumentSnapshot()` and `convertQuerySnapshot()` functions, so that it convert documentSnapshot and querySnapshot to your data class and listof data class respectively. If you want a singleton class then create a `companion object`. In this case i am creating a repo class for user.
##### Note: make sure to import right package i.e `import com.sach.mark42.firestoreassistant.FirestoreRepo`

```
class UserRepo: FirestoreRepo<User>(){
    override fun convertDocumentSnapshot(documentSnapshot: DocumentSnapshot?): User? {
        return documentSnapshot?.toObject(User::class.java)
    }

    override fun convertQuerySnapshot(querySnapshot: QuerySnapshot?): List<User>? {
        val documents = querySnapshot?.documents?.mapNotNull {
            try {
                it.toObject(User::class.java)
            } catch (e: Exception) {
                null
            }
        }
        return documents
    }

    companion object {
        private var instance : UserRepo? = null

        fun getInstance(): UserRepo {
            if (instance == null)
                instance = UserRepo()

            return instance!!
        }
        fun collectionPath() = "users"
    }
}
```

**Java User**

Create a Repository class and extend `FirestoreRepo<T>()` class (use your model class in case of T). Override `convertDocumentSnapshot()` and `convertQuerySnapshot()` methods, so that it convert documentSnapshot and querySnapshot to your model class and listof model class respectively. If you want a singleton class then create a `static` method. In this case i am creating a repo class for user.
##### Note: make sure to import right package i.e `import com.sach.mark42.firestoreassistant.java.FirestoreRepo;`

```
public class UserRepo extends FirestoreRepo<User> {

    private static UserRepo INSTANCE;

    @Nullable
    @Override
    public User convertDocumentSnapshot(@Nullable DocumentSnapshot documentSnapshot) {
        return documentSnapshot.toObject(User.class);
    }

    @Nullable
    @Override
    public List<User> convertQuerySnapshot(@Nullable QuerySnapshot value) {
        ArrayList<User> users = new ArrayList<>();
        for (DocumentSnapshot snapshot: value.getDocuments()) {
            try {
                User user = snapshot.toObject(User.class);
                users.add(user);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return users;
    }

    public static UserRepo getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UserRepo();
        }
        return INSTANCE;
    }
}
```

* Create a `ViewModel` class and create a instance of your repo class. Implement some function as for your requirement. In this case i am creating a `UserViewModel` class. Use kotlin corutines for all the `suspend` function.

```
class UserViewModel : ViewModel() {

    private val userRepo by lazy {
        UserRepo.getInstance()
    }

    fun getUserFromFirestore(activity: AppCompatActivity) {
        val fName = activity.findViewById<TextView>(R.id.firstName)
        val lName = activity.findViewById<TextView>(R.id.lastName)
        val email = activity.findViewById<TextView>(R.id.email)
        userRepo.getDocumentFromFirestore(UserRepo.collectionPath(), "qUiuSLFQXTqYbEf6mxBV").observe(activity, Observer { user ->
            fName.text = user?.firstName
            lName.text = user?.lastName
            email.text = user?.email
        })
    }

    fun getUserFromCache(activity: AppCompatActivity) {
        viewModelScope.launch {
            val fName = activity.findViewById<TextView>(R.id.firstName)
            val lName = activity.findViewById<TextView>(R.id.lastName)
            val email = activity.findViewById<TextView>(R.id.email)
            val user = userRepo.getDocumentFromFirestoreCache(UserRepo.collectionPath(), "qUiuSLFQXTqYbEf6mxBV")
            fName.text = user?.firstName
            lName.text = user?.lastName
            email.text = user?.email
        }
    }

    fun pushUserToFirestore(user: User) {
        viewModelScope.launch {
            //it'll return the firestore push id key
            val key = userRepo.pushToFirestore(UserRepo.collectionPath(), user)
            user.userPushId = key
            // show confirmation message to user
        }
    }
}
```

`userRepo.getDocumentFromFirestore()` return a `LiveData<T>` so that you will get realtime updates if anything changes happen in firestore. you need to observe it and update your UI when ever there is some change in firestore.

`userRepo.getDocumentFromFirestoreCache()` return `T` (in this case `User`) from your local database. It will not make any request to your Firebase firestore. 

`userRepo.pushToFirestore()` push your data class to database and it'll return the pushId. you can save pushId if you need it later.

For more detail refer to [UserViewModel.kt](https://github.com/Im-Mark42/FirebaseFirestoreAssistant/blob/master/app/src/main/java/com/sach/mark42/firestoredemo/UserViewModel.kt)

**Java User**

Create a `ViewModel` class and create a instance of your repo class. Implement some function as for your requirement. In this case i am creating a `UserViewModel` class. Use `AsyncTask` to call the async methods and use `Livedata<T>` to update the UI.

```
public class UserViewModel extends ViewModel {

    private UserRepo userRepo = UserRepo.getInstance();

    public void getUserFromDatabase(AppCompatActivity activity) {
        final TextView fName = activity.findViewById(R.id.firstName);
        final TextView lName = activity.findViewById(R.id.lastName);
        final TextView email = activity.findViewById(R.id.email);

        userRepo.getDocumentFromFirestore("users", "qUiuSLFQXTqYbEf6mxBV")
                .observe(activity, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                fName.setText(user.getFirstName());
                lName.setText(user.getLastName());
                email.setText(user.getEmail());
            }
        });
    }

    public void getUserFromCache(final AppCompatActivity activity) {
        getUserFromCache("users", "qUiuSLFQXTqYbEf6mxBV")
                .observe(activity, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                TextView fName = activity.findViewById(R.id.firstName);
                TextView lName = activity.findViewById(R.id.lastName);
                TextView email = activity.findViewById(R.id.email);

                fName.setText(user.getFirstName());
                lName.setText(user.getLastName());
                email.setText(user.getEmail());
            }
        });
    }

    public void pushUserToFirestore(AppCompatActivity activity, String collectionPath, User user) {
        pushUserToFirestore(collectionPath, user).observe(activity, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                //it return the firebase push id key
                String key = s;
                // show confirmation message to user
            }
        });
    }
    
    private static LiveData<User> getUserFromCache(final String collectionPath, final String documentPath) {
        final UserRepo userRepo = UserRepo.getInstance();
        final MutableLiveData<User> liveData = new MutableLiveData<>();
        new AsyncTask<Void, Void, LiveData<User>>() {

            @Override
            protected LiveData<User> doInBackground(Void... voids) {
                try {
                    User result = userRepo.getDocumentFromFirestoreCache(collectionPath, documentPath).get();
                    liveData.postValue(result);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                return liveData;
            }
        }.execute();

        return liveData;
    }

    private static LiveData<String> pushUserToFirestore(final String collectionPath, final User user) {
        final UserRepo userRepo = UserRepo.getInstance();
        final MutableLiveData<String> liveData = new MutableLiveData<>();
        new AsyncTask<Void, Void, LiveData<String>>() {
            @Override
            protected LiveData<String> doInBackground(Void... voids) {
                try {
                    String key = userRepo.pushToFirestore(collectionPath, user).get();
                    liveData.postValue(key);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                return liveData;
            }
        }.execute();
        return liveData;
    }
}
```

`userRepo.getDocumentFromFirestore()` return a `LiveData<T>` so that you will get realtime updates if anything changes happen in firestore. you need to observe it and update your UI when ever there is some change in firestore.

`userRepo.getDocumentFromFirestoreCache().get()` return `T` (in this case `User`) from your local database. Then update your UI using `LiveData<T>`. It will not make any request to your Firebase firestore. 

`pushToFirestore().get()` push your data class to firestore and it'll return the pushId. you can save pushId if you need it later.

For more detail refer to [UserViewModel.java](https://github.com/Im-Mark42/FirebaseFirestoreAssistant/blob/master/app/src/main/java/com/sach/mark42/firestoredemo/java/UserViewModel.java)

* Initialize `ViewModel` class in your `activity` class and call above methods.

```
val viewModel = ViewModelProvider(this).get(UserViewModel::class.java)
```

For more detail refer to [MainActivity.kt](https://github.com/Im-Mark42/FirebaseFirestoreAssistant/blob/master/app/src/main/java/com/sach/mark42/firestoredemo/MainActivity.kt)

**Java User**

```
final UserViewModel userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
```

For more detail refer to [JavaActivity.java](https://github.com/Im-Mark42/FirebaseFirestoreAssistant/blob/master/app/src/main/java/com/sach/mark42/firestoredemo/java/JavaActivity.java)

### Work with list of data

* In `UserViewModel` class, implement below function. Then update your adapter.

```
fun getUsersFromFirestore(activity: AppCompatActivity) {
        userRepo.getCollectionFromFirestore(UserRepo.collectionPath()).observe(activity, Observer { users ->
            users?.forEach {
                // update your adapter in activity
            }
        })
}

fun getUsersFromCache() {
        viewModelScope.launch {
            val users = userRepo.getCollectionFromFirestoreCache(UserRepo.collectionPath())
            users?.forEach {
                // update your adapter in activity
            }
        }
}
```

**Java User**

In `UserViewModel` class, implement below function. Then update your adapter.

```
public void getUsersFromDatabase(AppCompatActivity activity) {
        userRepo.getCollectionFromFirestore("users").observe(activity, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                for (User user : users) {
                    // update your adapter in activity
                }
            }
        });
}

public void getUsersFromCache(AppCompatActivity activity) {
        getUsersFromCache().observe(activity, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                for (User user : users) {
                    // update your adapter in activity
                }
            }
        });
}

private static LiveData<List<User>> getUsersFromCache() {
        final UserRepo userRepo = UserRepo.getInstance();
        final MutableLiveData<List<User>> liveData = new MutableLiveData<>();
        new AsyncTask<Void, Void, LiveData<List<User>>>() {

            @Override
            protected LiveData<List<User>> doInBackground(Void... voids) {
                try {
                    List<User> result = userRepo.
                            getCollectionFromFirestoreCache("users").get();
                    liveData.postValue(result);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                return liveData;
            }
        }.execute();

        return liveData;
}
```

* If you want to filter your data in database then use `query` method. `userRepo.getQueryFromFirestore()` function will return `LiveData<List<T>>` while `userRepo.getQueryFromFirestoreCache()` return `List<T>` from Firestore cache. Then update your adapter or UI.

```
fun queryUsersFromFirestore(activity: AppCompatActivity) {
        val query = FirebaseFirestore.getInstance().collection("users").
            orderBy(FIRESTORE_KEY.USER.firstName).
            whereEqualTo(FIRESTORE_KEY.USER.lastName, "Sahu").
            limitToLast(2)
        userRepo.getQueryFromFirestore(query).observe(activity, Observer { users ->
            users?.forEach {
                // update your adapter in activity
            }
        })
}

fun queryUsersFromCache() {
        val query = FirebaseFirestore.getInstance().collection("users").
            whereEqualTo(FIRESTORE_KEY.USER.firstName, "Sachi").
            whereEqualTo(FIRESTORE_KEY.USER.lastName, "Sahu")
        viewModelScope.launch {
            val users = userRepo.getQueryFromFirestoreCache(query)
            users?.forEach {
                // update your adapter in activity
            }
        }
}
```

**Java User**
 
If you want to filter your data in database then use `query` method. `userRepo.getQueryFromFirestore()` function will return `LiveData<List<T>>` while `userRepo.getQueryFromFirestoreCache().get()` return `List<T>` from Firestore cache. Then update your adapter or UI.

```
public void queryUsersFromDatabase(AppCompatActivity activity) {
        Query query = FirebaseFirestore.getInstance().collection("users").
                whereEqualTo(FIRESTORE_KEY.USER.firstName, "Sachi").
                whereEqualTo(FIRESTORE_KEY.USER.lastName, "Sahu");
        userRepo.getQueryFromFirestore(query).observe(activity, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                for (User user : users) {
                    // update your adapter in activity
                }
            }
        });
}

public void queryUsersFromCache(AppCompatActivity activity) {
        Query query = FirebaseFirestore.getInstance().collection("users").
                orderBy(FIRESTORE_KEY.USER.firstName).
                startAt("Sach").
                endAt("Sach" + "\uf8ff");
        queryUsersFromCache(query).observe(activity, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                for (User user : users) {
                    // update your adapter in activity
                    Log.v("getFromSCache", user.getFirstName());
                }
            }
        });
}

private static LiveData<List<User>> queryUsersFromCache(final Query query) {
        final UserRepo userRepo = UserRepo.getInstance();
        final MutableLiveData<List<User>> liveData = new MutableLiveData<>();
        new AsyncTask<Void, Void, LiveData<List<User>>>() {

            @Override
            protected LiveData<List<User>> doInBackground(Void... voids) {
                try {
                    List<User> result = userRepo.getQueryFromFirestoreCache(query).get();
                    liveData.postValue(result);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                return liveData;
            }
        }.execute();

        return liveData;
}
```

#### Thank you for using FirebaseFirestoreAssistant.
