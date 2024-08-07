# NOTE: Native-platform is not available on Linux with musl libc (for alpine even gcompat package don't help)
FROM gradle:8.9.0-jdk21 AS build

RUN apt-get update && apt-get -y --no-install-recommends install \
    build-essential

COPY . /src
WORKDIR /src

RUN \
    --mount=type=cache,target=/src/.gradle,rw \
    --mount=type=cache,target=/src/bin/build,rw \
    --mount=type=cache,target=/home/gradle/.gradle,rw \
    gradle --debug linkReleaseExecutableNative

RUN ln -sn linuxX64 build/bin/linux-amd64 && \
    ln -sn linuxArm64 build/bin/linux-arm64

FROM ubuntu:22.04

COPY --from=build build/bin/linux-${TARGETARCH}/releaseExecutable/kbre.kexe /kbre

ENTRYPOINT ["/kbre"]
