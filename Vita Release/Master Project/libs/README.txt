
####Create a folder called "libs" under "app" in Android Studio and copy all the libraries into the libs folder.

dependencies {
    compile fileTree(dir: 'libs', include: ['*.jar'])
    compile files('libs/resty-0.3.2.jar')
    compile files('libs/commons-io-2.4.jar')
}