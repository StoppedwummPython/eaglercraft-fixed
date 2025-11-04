#!/bin/bash
sh build_compile_buildtools.sh

wget "http://www.modcoderpack.com/files/mcp918.zip" -O mcp918.zip
cp mcp918.zip mcp918/mcp918.zip
wget "https://launchermeta.mojang.com/v1/packages/f6ad102bcaa53b1a58358f16e376d548d44933ec/1.8.json" -O mcp918/1.8.8.json
wget "https://launcher.mojang.com/v1/objects/0983f08be6a4e624f5d85689d1aca869ed99c738/client.jar" -O mcp918/1.8.8.jar

java -jar buildtools/BuildTools.jar init --build
java -jar buildtools/BuildTools.jar workspace --build

ls .eagworkspace

cd .eagworkspace

chmod +x gradlew
./gradlew makeMainWasmClientBundle
./gradlew makeMainOfflineDownload

mkdir ../out

cp -r target_teavm_javascript/javascript ../out/eaglercraft_js_client/
cp -r target_teavm_wasm_gc/javascript_dist ../out/eaglercraft_wasm_client/

cd ..

ls out

echo "Build complete! Output is in the 'out' directory."