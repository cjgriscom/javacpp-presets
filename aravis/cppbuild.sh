#!/bin/bash
# This file is meant to be included by the parent cppbuild.sh script
if [[ -z "$PLATFORM" ]]; then
    pushd ..
    bash cppbuild.sh "$@" aravis
    popd
    exit
fi

ARAVIS_VERSION=0.8.22
MESON_VERSION=0.59.2
mkdir -p $PLATFORM
cd $PLATFORM
case $PLATFORM in
    linux-x86_64)
	ARAVIS_ARCH=x86_64
        ;;
    linux-arm64)
	ARAVIS_ARCH=x86_64
        ;;
    *)
        echo "Error: Platform \"$PLATFORM\" is not supported"
        ;;
esac

download https://github.com/AravisProject/aravis/releases/download/${ARAVIS_VERSION}/aravis-${ARAVIS_VERSION}.tar.xz aravis-${ARAVIS_VERSION}.tar.xz
tar -xf aravis-${ARAVIS_VERSION}.tar.xz
download https://github.com/mesonbuild/meson/releases/download/${MESON_VERSION}/meson-${MESON_VERSION}.tar.gz meson-${MESON_VERSION}.tar.gz
tar -xf meson-${MESON_VERSION}.tar.gz
pushd meson-${MESON_VERSION}
./packaging/create_zipapp.py --outfile meson.pyz --interpreter '/usr/bin/env python3' ./
MESON=`realpath ./meson.pyz`
popd

pushd aravis-${ARAVIS_VERSION}
mkdir ../install || true
$MESON build -Dviewer=disabled -Dusb=disabled -Dintrospection=disabled -Dgst-plugin=disabled -Dprefix=`realpath ../install`
pushd build
ninja
ninja install
popd
popd
rm include || true
rm lib64 || true
rm bin || true
ln -s install/include/aravis-0.8 include
ln -s install/lib64 lib64
ln -s install/bin bin
if [ -f install/patch.h ]; then
  echo Patch already applied
else
  pushd install/include/aravis-0.8
    patch -p1 -i ../../../../../patches.patch
    cp ../../../../../extra.h ./
  popd
fi

cd ..
