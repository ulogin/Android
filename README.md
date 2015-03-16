


Android Studio SDK
==================

    
    






*	Поддерживаемые Операционные Системы: Android 2.1+
*	Поддерживаемые ориентации: portrait, landscape.
*	Локализации: русская, украинская, английская, французская, немецкая.
*	SDK предназначена для интеграции в Android-приложения разработчиками мобильных приложений.
*	Поддерживаются все социальные сети, реализуемые uLogin.
*	Текущая online-версия этого документа: http://ulogin.ru/help.php#androidstudiosdk
*	Скачать Android SDK можно здесь https://github.com/ulogin/Android/





Подключение библиотеки к проекту
==================


Для добавления библиотеки в ваш проект в среде **Android Studio** скопируйте файл **ulogin-sdk-vXX.aar** в каталог **libs** внутри дерева вашего проекта.
Например, в demo-приложении он находится в **UloginSDKDemo/app/libs/ulogin-sdk-v1.1.aar**.

Для того, чтобы **Gradle** начал использовать **uLogin SDK**, откройте скрипт сборки вашего приложения **build.gradle** и добавьте туда следующие строки:

```java
repositories {
    flatDir {
        dirs 'libs'
    }
}

```


Кроме того, добавьте библиотеку в зависимости в блок dependencies:

```java
compile(name:'ulogin-sdk-v1.1', ext:'aar')

```


Общий вид скрипта сборки получится примерно таким:


```java
apply plugin: 'com.android.application'

android {
    compileSdkVersion 22
    buildToolsVersion "21.1.2"

    defaultConfig {
        applicationId "ru.ulogin.uloginsdkdemo"
        minSdkVersion 8
        targetSdkVersion 22
        versionCode 1
        versionName "1.0"
    }
    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
        }
    }
}

repositories {
    flatDir {
        dirs 'libs'
    }
}

dependencies {
    compile fileTree(dir: 'libs', include: ['*.jar'])
    compile(name:'ulogin-sdk-v1.1', ext:'aar')
}

```



Откройте файл **AndroidManifest.xml** вашего проекта. Для корректной работы **uLogin SDK** там должны присутсвовать две записи.



Первая - внутри основного тега **manifest**, разрешение использовать интернет и считывать состояние телефона:

```java
<uses-permission android:name="android.permission.INTERNET"/>
<uses-permission android:name="android.permission.READ_PHONE_STATE"/>
```


Вторая - внутри тега application, объявление Activity для uLogin SDK:

```java
<activity android:name="ru.ulogin.sdk.UloginAuthActivity"
            android:configChanges="orientation|screenSize" />
```


Пример корректного **AndroidManifest.xml** можно посмотреть в каталоге **uLoginSDKDemo** в архиве с **uLogin SDK**.



Библиотека подключена, теперь можно использовать её в вашем приложении.






Открытие окна авторизации в приложении
==================


Для вызова окна авторизации необходимо создать **Intent** и запустить **Activity** **UloginAuthActivity**.
```java
Intent intent = new Intent(getApplicationContext(),UloginAuthActivity.class);
```


После создания **Intent** ему можно задать три параметра:

*	список доступных пользователю провайдеров авторизации. По умолчанию - все известные провайдеры. Например:
```java
intent.putExtra(
	UloginAuthActivity.PROVIDERS,
	new ArrayList(new String[]{"vkontakte","odnoklassniki","mailru"})
);
```

*	обязательные поля для авторизации. По умолчанию - **first_name**, **last_name**. Пример:
```java
intent.putExtra(
	UloginAuthActivity.FIELDS,
	new ArrayList(new String[]{"first_name","last_name"})
);
```

*	опциональные поля для авторизации. По умолчанию этот список пуст. Пример:
```java
intent.putExtra(
	UloginAuthActivity.OPTIONAL,
	new ArrayList(new String[]{"nickname","photo"})
);
```

*	идентификатор приложения. Необходим только в случае, если вы используете своё приложение для авторизации. По умолчанию не задан. Пример:
```java
intent.putExtra(
	UloginAuthActivity.APPLICATION_ID,
	"xxxxxx"
);
```

*	секретный ключ приложения. Необходим только в случае, если вы используете своё приложение для авторизации. По умолчанию не задан. Пример:
```java
intent.putExtra(
	UloginAuthActivity.SECRET_KEY,
	"xxxxxx"
);
```




Если какой-либо из параметров не задан, используется значение по умолчанию. Теперь **Intent** может быть запущен.




Пример вызова окна авторизации:

```java

public final int REQUEST_ULOGIN = 1; //можно задать константе любое другое значение

public void runUlogin() {
	Intent intent = new Intent(getApplicationContext(),UloginAuthActivity.class);

	String[] providers		= getResources().getStringArray(ru.ulogin.sdk.R.array.ulogin_providers);
	String[] mandatory_fields	= new String[] {"first_name", "last_name" };
	String[] optional_fields	= new String[] {"nickname","photo"};

	intent.putExtra(
		UloginAuthActivity.PROVIDERS,
		new ArrayList(Arrays.asList(providers))
		);
	intent.putExtra(
		UloginAuthActivity.FIELDS,
		new ArrayList(Arrays.asList(mandatory_fields))
		);
	intent.putExtra(
		UloginAuthActivity.OPTIONAL,
		new ArrayList(Arrays.asList(optional_fields))
		);

	startActivityForResult(intent, REQUEST_ULOGIN);
}

```



*	**ru.ulogin.sdk.R.array.ulogin_providers**	- список всех известных провайдеров;
*	**ru.ulogin.sdk.R.array.ulogin_fields**	- список всех известных полей.


Если в параметре PROVIDERS было передано несколько названий социальных сетей, то появится экран с возможностью выбора одной из них. Если же передано только одно название,
то сразу откроется окно входа в социальную сеть. Этим методом можно реализовать кастомизацию кнопок авторизации через социальные сети в вашем приложении.




Обработка результата авторизации
==================


После завершения работы окна авторизации управление возвращается в метод **onActivityResult** того **Activity**, который запустил **Intent** авторизации. Методу будет передано три параметра:

*	**requestCode** - код, указанный при нициализации **Intent**а;
*	**resultCode** - код завершения авторизации;
*	**intent** - объект **Intent** с данными авторизованного пользователя.


Если переменная **resultCode** имеет значение **RESULT_OK**, значит авторизация завершена успешно и переменная **intent** содержит пользовательские данные.
Значение **resultCode** **RESULT_CANCELED** означает, что либо пользователь отказался от авторизации, либо во время авторизации произошла ошибка.
Описание ошибки в последнем случае будет доступно в поле **error**.



Пользовательские данные возвращаются в виде **HashMap**:

```java
HashMap userdata = (HashMap) intent.getSerializableExtra(UloginAuthActivity.USERDATA);
```


В случае **RESULT_OK** ключами хэша являются имена полей, переданные при инициализации интента. При этом поля, указанные как обязательные, всегда будут присутствовать в этом хэше.
Необязательные поля могут отсутствовать.



В случае **RESULT_CANCELED** в хэше передаётся ключ **error**, содержащий причину отмены авторизации - строку **canceled** или текст ошибки.



Приведём пример реализации метода **onActivityResult**.


```java
@SuppressWarnings("unchecked")
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
	// requestCode должно сравниваться со значением константы,
	//   указанной при инициализации Intent
	if (requestCode == REQUEST_ULOGIN) {
		//получаем данные ответа:
		HashMap userdata =
			(HashMap) intent.getSerializableExtra (UloginAuthActivity.USERDATA);

		switch (resultCode) {
		case RESULT_OK:
			//если авторизация прошла успешно, то приветствуем пользователя
			Toast.makeText(this,
				"Hello, " + userdata.get("first_name") + " "
					+ userdata.get("last_name") + "!",
				Toast.LENGTH_SHORT).show();
			break;
		case RESULT_CANCELED:
			//если авторизация завершилась с ошибкой, то выводим причину
			if(userdata.get("error").equals("canceled")) {
				Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(this, "Error: "+userdata.get("error"),
					Toast.LENGTH_SHORT).show();
			}
		}
	}
}
```


Из кода видно, что в случае успешной авторизации этот метод приветствует пользователя по имени.



Пример готового приложения можно посмотреть в архиве с **uLogin SDK** в каталоге **uLoginSDKDemo**.
