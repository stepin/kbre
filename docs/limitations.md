# Limitations

- it's unclear how to set up unit tests for Kotlin Native properly (so, currently coverage is low as it's unclear
how it should be)
- maybe support of `libs.versions.toml` has problems in some corner cases: it's interesting to have examples
- it's unclear how to build Kotlin Native in Docker: in all cases it's not working for me (but maybe it's ram limitation, unclear). There is ticket about it: https://youtrack.jetbrains.com/issue/KT-39895/Missing-Documentation-On-Using-Kotlin-Native-With-Docker .
  So, for now, binaries are just added to Docker image.
- binary files detection is really simple now
- Alpine is not supported as Kotlin compiler don't work with `musl libc`. Even `gcompat` package don't fix it (most probably some detection problem).
- Kotlin Native compiler is not available for Linux ARM, only for macOS ARM. So, no ARM docker images. Missing file: https://repo.maven.apache.org/maven2/org/jetbrains/kotlin/kotlin-native-prebuilt/2.0.0/kotlin-native-prebuilt-2.0.0-linux-aarch64.tar.gz
  It means that we need to compile binaries on 1 supported arch, then add them into docker images.
