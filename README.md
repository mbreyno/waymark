<p align="center">
  <img src="site/icon.png" width="128" alt="Waymark icon" style="image-rendering: pixelated">
</p>

<h1 align="center">Waymark</h1>

<p align="center">A simple <code>/home</code> mod for Fabric servers · Minecraft 26.1.2 – 26.2</p>

<p align="center">
  <a href="https://waymark.homes">waymark.homes</a> ·
  <a href="https://modrinth.com/mod/waymark">Modrinth</a>
</p>

---

Type `/home` and a menu opens with three home slots. Click **New Home** to set a home right where you stand (you'll be asked to name it via the anvil screen). Click an existing home to **teleport**, **rename**, or **delete** it. Deleting frees the slot back to "New Home".

Waymark is 100% server-side — it uses vanilla container and anvil menus, so players on unmodded clients get the full experience.

## Repository layout

| Folder | Contents |
|---|---|
| [`mod/`](mod/) | The Fabric mod (Gradle + fabric-loom, Mojang official mappings) |
| [`site/`](site/) | One-page documentation site served at [waymark.homes](https://waymark.homes) |
| [`modrinth/`](modrinth/) | Modrinth submission package: description, icon, gallery images, checklist |

## Building

Requires Java 25+.

```sh
cd mod
./gradlew build
# jar lands in mod/build/libs/waymark-<version>.jar
```

The jar is built against 26.1.2 and also compiles cleanly against 26.2 (`./gradlew build -Pminecraft_version=26.2 -Pfabric_api_version=0.154.2+26.2`); one jar covers the whole supported range.

## Server install

1. Fabric Loader ≥ 0.19 on a Minecraft 26.1.2 – 26.2 server
2. Drop [Fabric API](https://modrinth.com/mod/fabric-api) and the Waymark jar into `mods/`
3. Restart — `/home` just works

Homes are stored per world in `waymark_homes.json`.

## License

[MIT](mod/LICENSE)
