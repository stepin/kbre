# Why?

If you try to use https://start.spring.io/ , https://kmp.jetbrains.com/ , https://start.ktor.io/ ,
or https://code.quarkus.io/ you will find out that:

1. it's not enough -- a lot of things needs to be configured after download
2. it's hard to update: update is done only manually

It's clear that for each company / team / individual templates should be different.
That's why https://github.com/stepin/kbre-default-config is done as starting point,
but you need to adapt it according to your preferences.

kbre tool uses your templates to create and update repos.

kbre started as Gradle macros tool, but now it doesn't depend on Gradle, and it can be used with any language.
