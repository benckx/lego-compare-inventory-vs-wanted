<a href="https://paypal.me/benckx/2">
<img src="https://img.shields.io/badge/Donate-PayPal-green.svg"/>
</a>

# About

When viewing Lego models on <a href="https://www.bricklink.com/">Bricklink</a>, it is possible to add all the necessary
parts to a "Wanted List" (and then use this list to purchase the parts). However, it doesn't seem possible to
automatically filter out the parts you already own (from the sets you already own) from those "Wanted List".

So I wrote a script that updates the "Wanted List" XML file by taking into account the parts you already own (based on
the inventory generated from <a href="https://rebrickable.com/">Rebrickable</a>). You can then upload the updated XML on
Bricklink to create a new "Wanted List" and purchase the parts from this list instead.

Maybe there's already a more simple solution directly on Bricklink, but I could not find it (if you know the solution,
feel free to open an issue). There are options on Bricklink to add and part out sets, but it doesn't seem taken into
account while making a "Wanted List". But maybe I'm missing something obvious.

In any case I'll be able to do more custom things with my parts lists this way.

# How To

## on Rebrickable

First add all the sets you own: go to "My LEGO" -> "My Sets Lists". Create a list and add your sets. This will allow
Rebrickable to create an inventory of all the parts you own. You can also add individual parts.

You can then export your inventory from Rebrickable in the BrickLink format (go to "My LEGO" -> "All my parts" -> "
Export Parts" -> "BrickLink XML") and replace `data/inventory.xml` by the export result.

## on Bricklink

Go to your "Wanted List" and click "Download". Move the file under the `data` folder.

## In Code

Replace the "Wanted List" XML file accordingly:

```kotlin
fun main() {
    val filterer = InventoryFilterer("data/razor-crest-75331.xml")
    filterer.execute()
}
```

The script will output 2 files:

- `data/razor-crest-75331-updated.xml` where the parts you own have been subtracted
- `data/razor-crest-75331-output-report.csv` CSV report in the following format, so you can double-check the
  modifications:

| modification type | part id | color code | color             | qty needed | qty inventory | qty missing |
|-------------------|---------|------------|-------------------|------------|---------------|-------------|
| sufficient        | 18649   | 86         | Light Bluish Gray | 4          | 4             | 0           |
| sufficient        | 22484   | 85         | Dark Bluish Gray  | 1          | 2             | 0           |
| ...               | ...     | ...        | ...               | ...        | ...           | ...         |
| insufficient      | 10247   | 11         | Black             | 6          | 2             | 4           |

# Ideas for future iterations

- Implement some kind of rules engine that allow for color substitution (e.g. "use dark grey if I don't have the part in
  black") or part substitution (e.g. "use two 2x2 brick if I don't have one 2x4 brick") (something like that already
  exists in the Studio app of Bricklink)
- Maybe a UI and an executable, so it's user-friendly for everyone
