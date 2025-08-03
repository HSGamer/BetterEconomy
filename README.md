# BetterEconomy

A simple economy plugin

## Commands & Permissions

| Command                       | Permission                                            |
|-------------------------------|-------------------------------------------------------|
| `/eco give <player> <amount>` | bettereconomy.set                                     |
| `/eco take <player> <amount>` | bettereconomy.set                                     |
| `/eco set <player> <amount>`  | bettereconomy.set                                     |
| `/eco reload`                 | bettereconomy.reload                                  |
| `/balance`                    | bettereconomy.balance<br>bettereconomy.balance.others |
| `/baltop`                     | bettereconomy.balancetop                              |
| `/pay <player> <amount>`      | bettereconomy.pay                                     |

## Config

```yaml
# The file handler to store the balance
# Allow: file, mysql, sqlite, json
handler-type: file

# Enable if the plugin should hook to other economy cores (Vault, Treasury, etc)
hook-enabled: true

# Settings on formatting the balance
currency:
  symbol: $
  singular: $
  plural: $
  format-fractional-digits: 2

# Settings on balance tasks
balance:
  top-update-period: 100
  file-save-period: 200
  start-amount: 0.0
  min-amount: 0.0

# Settings on database
database:
  mysql:
    host: localhost
    port: '3306'
    dbname: ''
    username: root
    password: ''
```

## Placeholders

> Requires [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/)

| Placeholder                                     | Description                                                                 |
|-------------------------------------------------|-----------------------------------------------------------------------------|
| `%bettereconomy_balance%`                       | Player's current balance                                                    |
| `%bettereconomy_balance_formatted%`             | Player's balance (formatted)                                                |
| `%bettereconomy_balance_formatted_short%`       | Player's balance in short notation (e.g. 1.3k)                              |
| `%bettereconomy_top%`                           | Player's current top position                                               |
| `%bettereconomy_top_name_<number>%`             | Name of the player at position `<number>` (starting from 1)                 |
| `%bettereconomy_top_uuid_<number>%`             | UUID of the player at position `<number>` (starting from 1)                 |
| `%bettereconomy_top_balance_<number>%`          | Balance of the player at position `<number>` (starting from 1)              |
| `%bettereconomy_top_balance_formatted_<number>%`| Formatted balance of the player at position `<number>` (starting from 1)    |
