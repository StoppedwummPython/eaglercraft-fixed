sh build_compile_buildtools.sh

wget "http://www.modcoderpack.com/files/mcp918.zip" -O mcp918.zip
cp mcp918.zip mcp918/mcp918.zip
wget "https://launchermeta.mojang.com/v1/packages/f6ad102bcaa53b1a58358f16e376d548d44933ec/1.8.json" -O mcp918/1.8.8.json
wget "https://launcher.mojang.com/v1/objects/0983f08be6a4e624f5d85689d1aca869ed99c738/client.jar" -O mcp918/1.8.8.jar

java -jar buildtools/BuildTools.jar init --build
java -jar buildtools/BuildTools.jar workspace --build