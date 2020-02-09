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
          
          implementation 'com.github.Im-Mark42:FirebaseFirestoreAssistant::0.1-Beta'
          
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

* Create a Repository class and extend `FirestoreRepo<T>()` class (use your data class in case of T). Override `convertDocumentSnapshot()` function and provide your data class, so that it convert dataSnapshot to your data class. If you want a singleton class then create a `companion object`. In this case i am creating a repo class for user.
##### Note: make sure to import right package i.e `import com.sach.mark42.firestoreassistant.FirestoreRepo`

```
class UserRepo: FirestoreRepo<User>(){
    override fun convertDocumentSnapshot(documentSnapshot: DocumentSnapshot?): User? {
        return documentSnapshot?.toObject(User::class.java)
    }

    override fun convertQuerySnapshot(querySnapshot: QuerySnapshot?): User? {
        return null
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

Create a Repository class and extend `FirestoreRepo<T>()` class (use your model class in case of T). Override `convertDocumentSnapshot()` method and provide your model class, so that it convert dataSnapshot to your model class. If you want a singleton class then create a `static` method. In this case i am creating a repo class for user.
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
    public User convertQuerySnapshot(@Nullable QuerySnapshot querySnapshot) {
        return null;
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

    val userRepo by lazy {
        UserRepo.getInstance()
    }

    fun getUserFromDatabase(activity: AppCompatActivity) {
        val fName = activity.findViewById<TextView>(R.id.firstName)
        val lName = activity.findViewById<TextView>(R.id.lastName)
        val email = activity.findViewById<TextView>(R.id.email)
        
        userRepo.getFromDatabase(UserRepo.userPath("-LxjJXsJdbZiEjjh1A89")).observe(activity, Observer { user ->
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
            
            val user = userRepo.getFromDatabaseCache(UserRepo.userPath("-LxjJXsJdbZiEjjh1A89"))
            
            fName.text = user?.firstName
            lName.text = user?.lastName
            email.text = user?.email
        }
    }

    fun pushUserToDatabase(user: User) {
        viewModelScope.launch {
            //it return the firebase push id key
            val key = userRepo.pushToDatabase(UserRepo.path(), user)
            user.userPushId = key
            // show confirmation message to user
        }
    }
}
```

`userRepo.getFromDatabase()` return a `LiveData<T>` so that you will get realtime updates if anything changes happen in database. you need to observe it and update your UI when ever there is some change in database.

`userRepo.getFromDatabaseCache()` return `T` (in this case `User`) from your local database. It will not make any request to your Firebase database. 

`pushUserToDatabase(user: User)` push your data class to database and it'll return the pushId. you can save pushId if you need it later.

For more detail refer to [UserViewModel.kt](https://www.github.com/Im-Mark42/FirebaseDatabaseAssistant/tree/master/app/src/main/java/com/sach/mark42/databasedemo/UserViewModel.kt)

**Java User**

Create a `ViewModel` class and create a instance of your repo class. Implement some function as for your requirement. In this case i am creating a `UserViewModel` class. Use `AsyncTask` to call the async methods and use `Livedata<T>` to update the UI.

```
public class UserViewModel extends ViewModel {

    private UserRepo userRepo = UserRepo.getInstance();

    public void getUserFromDatabase(AppCompatActivity activity) {
        final TextView fName = activity.findViewById(R.id.firstName);
        final TextView lName = activity.findViewById(R.id.lastName);
        final TextView email = activity.findViewById(R.id.email);

        userRepo.getFromDatabase("users/-LxjJXsJdbZiEjjh1A89").observe(activity, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                fName.setText(user.getFirstName());
                lName.setText(user.getLastName());
                email.setText(user.getEmail());
            }
        });
    }

    public void getUserFromCache(final AppCompatActivity activity) {
        getUserFromCache("users/-LxjJXsJdbZiEjjh1A89").observe(activity, new Observer<User>() {
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

    public void pushUserToDatabase(AppCompatActivity activity, User user) {
        pushUserToDatabase(user).observe(activity, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                //it return the firebase push id key
                String key = s;
                // show confirmation message to user
            }
        });
    }
    
    private static LiveData<User> getUserFromCache(final String path) {
        final UserRepo userRepo = UserRepo.getInstance();
        final MutableLiveData<User> liveData = new MutableLiveData<>();
        new AsyncTask<Void, Void, LiveData<User>>() {

            @Override
            protected LiveData<User> doInBackground(Void... voids) {
                try {
                    User result = userRepo.getFromDatabaseCache(path).get();
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

    private static LiveData<String> pushUserToDatabase(final User user) {
        final UserRepo userRepo = UserRepo.getInstance();
        final MutableLiveData<String> liveData = new MutableLiveData<>();
        new AsyncTask<Void, Void, LiveData<String>>() {
            @Override
            protected LiveData<String> doInBackground(Void... voids) {
                try {
                    String key = userRepo.pushToDatabase("users", user).get();
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

`userRepo.getFromDatabase()` return a `LiveData<T>` so that you will get realtime updates if anything changes happen in database. you need to observe it and update your UI when ever there is some change in database.

`userRepo.getFromDatabaseCache().get()` return `T` (in this case `User`) from your local database. Then update your UI using `LiveData<T>`. It will not make any request to your Firebase database. 

`pushToDatabase().get()` push your data class to database and it'll return the pushId. you can save pushId if you need it later.

For more detail refer to [UserViewModel.java](https://www.github.com/Im-Mark42/FirebaseDatabaseAssistant/tree/master/app/src/main/java/com/sach/mark42//databasedemo/java/UserViewModel.java)

* Initialize `ViewModel` class in your `activity` class and call above methods.

```
val viewModel = ViewModelProvider(this).get(UserViewModel::class.java)
```

For more detail refer to [MainActivity.kt](https://www.github.com/Im-Mark42/FirebaseDatabaseAssistant/tree/master/app/src/main/java/com/sach/mark42/databasedemo/MainActivity.kt)

**Java User**

```
final UserViewModel userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
```

For more detail refer to [JavaActivity.java](https://www.github.com/Im-Mark42/FirebaseDatabaseAssistant/tree/master/app/src/main/java/com/sach/mark42/databasedemo/java/JavaActivity.java)

### Work with list of data

* Create a Repository class and extend `DatabaseRepo<T>()` and replace `T` with `List<User>` (use your data class in case of `User`). Override `convertDatabaseSnapshot()` and since `dataSnapshot` has list of data, you need to loop through the `dataSnapshot.children`. Use `companion object` for Singleton class.
##### Note: make sure to import right package i.e `import com.sach.mark42.databaseassistant.DatabaseRepo`

```
class UsersRepo : DatabaseRepo<List<User>>() {
    override fun convertDatabaseSnapshot(dataSnapshot: DataSnapshot?): List<User>? {
        val users = dataSnapshot?.children?.map {
            try {
                val user = it.getValue(User::class.java)
                user
            } catch (e: Exception) {
                null
            }
        }?.filterNotNull()

        return users
    }

    companion object {
        private var instance : UsersRepo? = null

        fun getInstance(context: Context): UsersRepo {
            if (instance == null)
                instance = UsersRepo(context)

            return instance!!
        }
        fun path() = "users"
    }
}
```

**Java User**

Create a Repository class and extend `DatabaseRepo<T>()` and replace `T` with `List<User>` (use your data class in case of `User`). Override `convertDatabaseSnapshot()` and since `dataSnapshot` has list of data, you need to loop through the `dataSnapshot.getChildren()`. If you want a singleton class then create a `static` method.
##### Note: make sure to import right package i.e `import com.sach.mark42.databaseassistant.java.DatabaseRepo;`

```
public class UsersRepo extends DatabaseRepo<List<User>> {

    private static UsersRepo INSTANCE;

    @Nullable
    @Override
    public List<User> convertDatabaseSnapshot(@Nullable DataSnapshot dataSnapshot) {
        ArrayList<User> users = new ArrayList<>();
        for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
            try {
                User user = snapshot.getValue(User.class);
                users.add(user);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return users;
    }

    public static UsersRepo getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UsersRepo();
        }
        return INSTANCE;
    }
}
```

* Create a `ViewModel` class and create a instance of your repo class. Implement some function as for your requirement. In this case i am creating a `UsersViewModel` class.

```
class UsersViewModel : ViewModel() {

    val usersRepo by lazy {
        UsersRepo.getInstance()
    }

    fun getUsersFromDatabase(activity: AppCompatActivity) {
        usersRepo.getFromDatabase(UsersRepo.path()).observe(activity, Observer { users ->
            users?.forEach {
                //update UI
            }
        })
    }

    fun getUsersFromCache() {
        viewModelScope.launch {
            val users = usersRepo.getFromDatabaseCache(UsersRepo.path())
            users?.forEach {
                //update UI
            }
        }
    }
}
```

**Java User**

Create a `ViewModel` class and create a instance of your repo class. Implement some function as for your requirement. In this case i am creating a `UsersViewModel` class.

```
public class UsersViewModel extends ViewModel {
    private UsersRepo usersRepo = UsersRepo.getInstance();

    public void getUsersFromDatabase(AppCompatActivity activity) {
        usersRepo.getFromDatabase("users").observe(activity, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                for (User user : users) {
                    // update your adapter
                }
            }
        });
    }
    
    public void getUsersFromCache(AppCompatActivity activity) {
        getUsersFromCache().observe(activity, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                for (User user : users) {
                    // update your adapter
                }
            }
        });
    }
    
    private static LiveData<List<User>> getUsersFromCache() {
        final UsersRepo usersRepo = UsersRepo.getInstance();
        final MutableLiveData<List<User>> liveData = new MutableLiveData<>();
        new AsyncTask<Void, Void, LiveData<List<User>>>() {

            @Override
            protected LiveData<List<User>> doInBackground(Void... voids) {
                try {
                    List<User> result = usersRepo.getFromDatabaseCache("users").get();
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
}
```

* If you want to filter your data in database then use `query` method. `usersRepo.getQueryFromDatabase()` function will return `LiveData<T>` while `usersRepo.getQueryFromDatabaseCache()` return data from Firebase cache.

```
fun queryUsersFromDatabase(activity: AppCompatActivity) {
        val query = FirebaseDatabase.getInstance().getReference("users").orderByKey().limitToLast(2)
        usersRepo.getQueryFromDatabase(query).observe(activity, Observer { users ->
            users?.forEach {
                //update UI
            }
        })
 }
 
 fun queryUsersFromCache() {
        viewModelScope.launch {
            val query = FirebaseDatabase.getInstance().getReference("users").orderByKey().limitToLast(2)
            val users = usersRepo.getQueryFromDatabaseCache(query)
            users?.forEach {
                //update UI
            }
        }
}
```

**Java User**
 
If you want to filter your data in database then use `query` method. `usersRepo.getQueryFromDatabase()` function will return `LiveData<T>` while `usersRepo.getQueryFromDatabaseCache().get()` return data from Firebase cache.

```
 public void queryUsersFromDatabase(AppCompatActivity activity) {
        Query query = FirebaseDatabase.getInstance().getReference("users")
                .orderByKey().limitToLast(2);
        usersRepo.getQueryFromDatabase(query).observe(activity, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                for (User user : users) {
                    // update your adapter
                }
            }
        });
 }
 
 public void queryUsersFromCache(AppCompatActivity activity) {
        Query query = FirebaseDatabase.getInstance().getReference("users")
                .orderByKey().limitToLast(2);
        queryUsersFromCache(query).observe(activity, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                for (User user : users) {
                    // update your adapter
                }
            }
        });
 }
 
 private static LiveData<List<User>> queryUsersFromCache(final Query query) {
        final UsersRepo usersRepo = UsersRepo.getInstance();
        final MutableLiveData<List<User>> liveData = new MutableLiveData<>();
        new AsyncTask<Void, Void, LiveData<List<User>>>() {

            @Override
            protected LiveData<List<User>> doInBackground(Void... voids) {
                try {
                    List<User> result = usersRepo.getQueryFromDatabaseCache(query).get();
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
 
* Initialize `ViewModel` class in your `activity` class and call above methods

```
val usersViewModel = ViewModelProvider(this).get(UsersViewModel::class.java)
```

**Java User**

```
final UsersViewModel usersViewModel = new ViewModelProvider(this).get(UsersViewModel.class);
```

#### Thank you for using FirebaseDatabaseAssistant.
