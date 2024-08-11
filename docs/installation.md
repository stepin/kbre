# Installation

## Configuration repository

It's better to fork default repository, and use it from the beginning.

Default configuration:
```shell
cd ~
git clone https://github.com/stepin/kbre-default-config .krbe
```

## Binary

### Installation using Brew

```shell
brew install stepin/tools/kbre
```

### Installation using Docker

```shell
alias kbre='docker run --rm -it -v $PWD:/data -w /data --user "$(id -u)" stepin/kbre'
kbre version
```

### Zip archive

Binaries can be downloaded from [GitHub Release page](https://github.com/stepin/kbre/releases).
