# Waymark

**A simple `/home` mod for Fabric servers.** Set up to 3 homes through a friendly in-game menu, then teleport, rename, or delete them — players don't install anything.

---

## ✨ How it works

Type **`/home`** and a menu opens with your three home slots.

- 🛏️ **Empty slots say "New Home".** Click one and you'll be asked to name it. Confirm, and the home is saved at the exact spot you were standing.
- 🌀 **Click an existing home** to open its options:
  - **Teleport** — go back to where the home was first created (works across dimensions).
  - **Rename** — give it a new name, shown right on the menu button.
  - **Delete** — free the slot; it goes back to "New Home".

That's the whole mod. No config, no permissions setup, no database.

## 🪶 100% server-side

Waymark uses vanilla container and anvil menus, so **players on completely unmodded clients get the full experience**. Just drop it in your server's `mods/` folder.

## 📦 Install

1. Install [Fabric Loader](https://fabricmc.net/use/server/) on your Minecraft **26.1.2 – 26.2** server.
2. Drop [Fabric API](https://modrinth.com/mod/fabric-api) and **Waymark** into `mods/`.
3. Restart. Players can use `/home` immediately.

## 🗃️ Storage

Homes are saved per world in `waymark_homes.json`, written on every change — restarts and crashes never lose a saved home.

## 🔗 Links

- 🌐 **Website:** [waymark.homes](https://waymark.homes)
- 🐙 **Source:** [github.com/mbreyno/waymark](https://github.com/mbreyno/waymark)
- 🐛 **Issues:** [github.com/mbreyno/waymark/issues](https://github.com/mbreyno/waymark/issues)
