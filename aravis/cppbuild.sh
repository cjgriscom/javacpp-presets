#!/bin/bash
# This file is meant to be included by the parent cppbuild.sh script
if [[ -z "$PLATFORM" ]]; then
    pushd ..
    bash cppbuild.sh "$@" aravis
    popd
    exit
fi

LANG=en_US.UTF-8
export LANG

GLIB_VERSION_MAJ=2.56
GLIB_VERSION_MIN=2.56.4
ARAVIS_VERSION=0.8.22
MESON_VERSION=0.59.2
mkdir -p $PLATFORM
cd $PLATFORM
case $PLATFORM in
    linux-x86_64)
	LIB_PATH=lib64
        ;;
    linux-arm64)
	LIB_PATH=lib/aarch64-linux-gnu
        ;;
    *)
        echo "Error: Platform \"$PLATFORM\" is not supported"
        ;;
esac

download https://download.gnome.org/sources/glib/$GLIB_VERSION_MAJ/glib-$GLIB_VERSION_MIN.tar.xz glib-$GLIB_VERSION_MIN.tar.xz
tar -xf glib-$GLIB_VERSION_MIN.tar.xz
download https://github.com/AravisProject/aravis/releases/download/${ARAVIS_VERSION}/aravis-${ARAVIS_VERSION}.tar.xz aravis-${ARAVIS_VERSION}.tar.xz
tar -xf aravis-${ARAVIS_VERSION}.tar.xz
download https://github.com/mesonbuild/meson/releases/download/${MESON_VERSION}/meson-${MESON_VERSION}.tar.gz meson-${MESON_VERSION}.tar.gz
tar -xf meson-${MESON_VERSION}.tar.gz
pushd meson-${MESON_VERSION}
./packaging/create_zipapp.py --outfile meson.pyz --interpreter '/usr/bin/env python3' ./
MESON=`realpath ./meson.pyz`
popd

pushd glib-${GLIB_VERSION_MIN}
mkdir ../install || true
$MESON build -Dprefix=`realpath ../install`
pushd build
ninja
ninja install
popd
popd

pushd aravis-${ARAVIS_VERSION}
mkdir ../install || true
PATH=$PATH:`realpath ../install/bin/` $MESON build -Dviewer=disabled -Dusb=disabled -Dintrospection=disabled -Dgst-plugin=disabled -Dlibdir=../install/$LIB_PATH -Dincludedir=../install/include -Dbindir=../install/bin -Dprefix=`realpath ../install` -Dpkg_config_path=`realpath ../install/$LIB_PATH/pkgconfig/`
pushd build
PATH=$PATH:`realpath ../../install/bin/` ninja
ninja install
popd
popd

if [[ -f "install/include/aravis-0.8/patch.h" ]]; then
  echo Patch already applied
else
  pushd install/include/aravis-0.8
    patch -p1 -i ../../../../../patches.patch
    cp ../../../../../extra.h ./
  popd
fi


rm -R include || true
mkdir include
rm -R lib || true
mkdir lib

cp glib-${GLIB_VERSION_MIN}/build/glib/glibconfig.h include/
cp -R install/include/aravis-0.8/* include/
cp -R install/include/glib-2.0/* include/

strip --strip-debug --strip-unneeded install/$LIB_PATH/libglib-2.0.so
strip --strip-debug --strip-unneeded install/$LIB_PATH/libgio-2.0.so
strip --strip-debug --strip-unneeded install/$LIB_PATH/libgmodule-2.0.so
strip --strip-debug --strip-unneeded install/$LIB_PATH/libgthread-2.0.so
strip --strip-debug --strip-unneeded install/$LIB_PATH/libgobject-2.0.so
strip --strip-debug --strip-unneeded install/$LIB_PATH/libaravis-0.8.so

cp -R install/$LIB_PATH/* lib/ || true

cd ..
