# Modrinth submission checklist

Everything in this folder is ready to paste/upload at https://modrinth.com/create/project

## Project settings

| Field | Value |
|---|---|
| **Name** | Waymark |
| **URL slug** | `waymark` |
| **Summary** (short description) | A simple /home mod for Fabric servers. Set up to 3 homes through an in-game menu, then teleport, rename, or delete them — no client mod required. |
| **Description** | paste `description.md` |
| **Icon** | upload `icon.png` (512×512) |
| **Categories** | Utility, Transportation |
| **Client side** | Optional (works in singleplayer, not needed to join servers) |
| **Server side** | Required |
| **License** | MIT |
| **Source URL** | https://github.com/mbreyno/waymark |
| **Issues URL** | https://github.com/mbreyno/waymark/issues |
| **Website** | https://waymark.homes |

## Gallery

| File | Title | Ordering |
|---|---|---|
| `gallery-banner.png` | Waymark — a /home mod for Fabric servers | 1 (set as featured) |
| `gallery-menu.png` | The /home menu | 2 |

## First version upload

| Field | Value |
|---|---|
| **File** | `waymark-1.0.0.jar` |
| **Version number** | 1.0.0 |
| **Version title** | Waymark 1.0.0 |
| **Channel** | Release |
| **Loaders** | Fabric |
| **Game versions** | 26.1.2, 26.2 |
| **Dependencies** | Fabric API (required) |

**Changelog for 1.0.0:**

```
Initial release.

- /home opens a menu with 3 home slots per player
- Click "New Home" to set a home where you stand and name it via the anvil screen
- Click an existing home to teleport, rename, or delete it
- Cross-dimension teleports supported
- Fully server-side: vanilla clients get the full experience
- One jar supports Minecraft 26.1.2 through 26.2
```
