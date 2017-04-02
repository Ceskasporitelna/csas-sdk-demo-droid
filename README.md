#CSAS SDK Demo for Android
This repository contains demo application demonstrating the usage of Android SDKs by Ceska Sporitelna a.s.

##SDKs being showcased
- [x] **[CoreSDK](https://github.com/Ceskasporitelna/cs-core-sdk-droid)** - Configuration of CoreSDK
- [x] **[CoreSDK/Locker](https://github.com/Ceskasporitelna/cs-core-sdk-droid/blob/master/docs/locker.md)** - Configuration of Locker & Event handling
- [x] **[LockerUI](https://github.com/Ceskasporitelna/cs-locker-ui-sdk-droid)** - LockerUI configuration and sample flow
- [x] **[Uniforms](https://github.com/Ceskasporitelna/cs-uniforms-sdk-droid)** - Uniforms configuration and sample flow
- [x] **[Places](https://github.com/Ceskasporitelna/cs-places-sdk-droid)** - Places configuration and sample flow
- [x] **[TransparentAccounts](https://github.com/Ceskasporitelna/cs-transparent-acc-sdk-droid)** - Transparent Accounts configuration and sample flow
- [x] **[AppMenu](https://github.com/Ceskasporitelna/cs-appmenu-sdk-droid)** - AppMenu configuration and sample flow


#[Changelog](CHANGELOG.md)

#Requirements
- Android 4.1+
- CSCoreSDK 0.9+
- CSLockerUI 0.9+
- CSUniformsSDK 0.9+
- CSPlacesSDK 0.9+
- CSTransparentAccSDK 0.10+
- CSAppMenuSDK 0.9+
- Gradle 2.8+
- Android Studio 1.5+

#Installation

**IMPORTANT!** You need to have your SSH keys registered with the GitHub since this repository is private.

1) Clone this repo using command `git clone https://github.com/Ceskasporitelna/csas-sdk-demo-droid.git`

2) Enter into cloned directory using command `cd csas-sdk-demo-droid`

3) Run command `git submodule update --init --recursive` to initialize and update required git submodules.

4) Open Android Studio and import this project. Add all detected unregistered VCS roots as Android Studio recommends. 

#Usage

##Running CSAS SDK Demo

To see how the demo application works, just open the project in 

Implementation of the application is in the file `csas-sdk-demo-droid/demo/src/main/java/cz/csas/demo/`. Pay special attention to `MainActivity.java` with all its comments to see how the frameworks are configured and used. The demo application focuses on UniformsSDK integration as well. See `UniformsActivity` with its two Fragments `FormListFragment`, `FormDescFragment`.

##Developing against CSAS SDK 
All CSAS SDKs are included in this repositories as submodules, you can thus use this demo project to test your implementation of bugfixes and new features. 


#Contributing
Contributions are more than welcome!

Please read our [contribution guide](CONTRIBUTING.md) to learn how to contribute to this project.

#Terms and License
Please read our [terms and conditions license](LICENSE.md)