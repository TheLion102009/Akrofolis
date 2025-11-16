# AkroFolis

AkroFolis (fork of Akropolis) — a modern hub/lobby plugin for Minecraft servers, fully compatible with Paper & Folia (API 1.21.x).

In short: AkroFolis provides a lightweight, modular hub with menus, scoreboards, holograms, nametags, vanish, hotbar items and more — optimized for Paper & Folia 1.21.0–1.21.10.

-----

Features
- Full compatibility with Paper & Folia API 1.21.x (1.21.0 through 1.21.10)
- Modular design: enable or disable modules (Scoreboard, Nametags, Holograms, Lobby, Vanish, etc.)
- Hooks: PlaceholderAPI support and optional integrations for scoreboard libraries and other services
- Menu system and server selector
- Configurable via `config.yml`, `messages.yml`, `commands.yml`, `data.yml`
- Folia-aware scheduler wrapper: uses region/global schedulers on Folia and falls back to paper schedulers when needed
- Open source and easy to fork or extend

-----

Installation
1. Build the plugin with Gradle (in project root):

   Windows (Powershell):
   .\gradlew.bat clean shadowJar

   The produced JAR will be located in `build/libs/` (for example `akrofolis-<version>.jar`).

2. Copy the JAR to your server's `plugins/` folder.
3. Start the server (Paper or Folia, 1.21.x).

Note: For consistency, name the plugin file `akrofolis-<version>.jar`.

-----

Configuration
- On first startup the plugin writes default config files to `plugins/AkroFolis/`: `config.yml`, `messages.yml`, `commands.yml`, `data.yml`.
- Edit `config.yml` to enable/disable modules and change settings.

Commands
- All plugin commands are registered under the `/akrofolis` prefix (for example `/akrofolis:fly`, `/akrofolis:lobby`).
- Legacy aliases (like `/akropolis`) can be kept or removed — see `commands.yml`.

API & Compatibility
- Supports Paper & Folia API 1.21.x (1.21.0–1.21.10).
- Internally uses a `SchedulerWrapper` to choose the best scheduler implementation available (Folia region/global scheduler or Paper sync scheduler).
- If you encounter scheduler-related errors (e.g. UnsupportedOperationException), make sure you run a Folia/Paper build that includes threaded regions and that external dependencies are present.

Dependencies
- PlaceholderAPI (recommended for placeholder support)
- Optional: scoreboard libraries or additional plugins; those may be declared as provided in `build.gradle.kts` and must be available at runtime if not shaded.

Troubleshooting
- UnsupportedOperationException when scheduling tasks on Folia: verify that `SchedulerWrapper` selects a Folia scheduler and that your server distribution supports threaded regions.
- `zip file closed` errors: remove duplicate or stale plugin JARs from `plugins/` and ensure the plugin JAR is not corrupted.
- ClassNotFoundException for external libraries: either shade the dependency into the plugin JAR or place the dependency JAR in `plugins/`.
- Module startup failures: check server logs for missing hooks, uninitialized state (NPEs), or scheduler conflicts.

Development & Contributing
- Fork the repository and create feature branches for changes.
- Open pull requests with a clear description and tests when possible.

Build (developer)
- Use the included Gradle wrapper:

   Windows (Powershell):
   .\gradlew.bat clean shadowJar

- Output: `build/libs/akrofolis-<version>.jar`.

Naming & Branding
- The plugin identifies as "AkroFolis" (capital F) in logs and plugin metadata.
- ASCII banner, plugin YAML entries and code references should reflect the new name and branding where appropriate.

License
- See the `LICENSE` file in the repository.

Contact
- Open issues and pull requests in the GitHub repository.
- For larger integrations (like third-party scoreboard libraries) please open an issue first to coordinate.

-----

Changelog (short)
- 1.9.3 — AkroFolis fork: Paper & Folia 1.21.x compatibility, branding updates, scheduler improvements, various bugfixes.

-----

Thanks for using AkroFolis! If you want, I can also add a CONTRIBUTING.md, issue/PR templates, or badges for build status and license.
