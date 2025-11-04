#!/bin/sh
sh run_chmod.sh
./build_compile_buildtools.sh
java -cp "buildtools/BuildTools.jar" net.lax1dude.eaglercraft.v1_8.buildtools.gui.CompileLatestClientGUI
rm -rf "##TEAVM.TMP##"