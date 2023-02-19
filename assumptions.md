# Assumptions

[[_TOC_]]

## The Character
- Can only travel on the path provided in the specification. 
- There can be a maximum of 8 cards at one time in a Character’s card slot 
- Can only equip one weapon, one armour, one helmet and one shield at a single time
- Will only battle enemies that come into the radius if they are into distance path-wise. That is, enemies can be in battle radius but not where the path doesn't reach.
- Has a max inventory of 10 + 4 (equipped)

## Capacity and Limits
- There cannot be more buildings than there are path and non-path tiles in the world.
- There cannot be more enemies than there are path tiles in the world.
- There cannot be more than 6 allies at one time.

## Items
- The health potion is sold at a price of 20 gold.
- The rare item “the one ring” is dropped at a rate of 1%.
- When max cards/equipment is exceeded, destroy old and give character 20 gold + 20 exp instead. 
- Every item is sold for 5 gold at the Hero’s Castle
- All items have a 5% chance of being dropped from enemies, 1% One True Ring, 1% SniperRifle, 5% chance of being obtained from cards. Gold has the remaining 34% chance of being dropped otherwise.
- Potions and items have a 5% chance of dropping on the floor at any time, outside of the Characters range
- Only 1 pile of gold and 1 potion can be dropped on the floor at any time
- Gold on the floor is randomised between 1-10 gold
- Doggie coin fluctates randomly between 0-50 gold, but exceeds this value when Elan is alive
- Compensation items from excess cards do not drop rare items, only commonly found ones.

## Cards
- All cards have a 3% chance of dropping when an enemy is killed. Only one card/item is dropped per enemy killed.

## Weapons
| Weapon | Price (gold) | Damage (Health points reduced) | Drop rate from slaying enemies |
| ------ | ------       | ------                         | ------                         |
| Sword  | 100          | 5                              | 4%                             |
| Stake  | 45           | 3 (6 to vampire)               | 5%                             |
| Staff  | 80           | 2                              | 4%                             |
| Andruil| N/A          | 10 (30 to bosses)              | 1%                             |

### Staff and Trance ability
- The Staff has a 20% chance of turning an enemy into an allied soldier per strike. 
- The trance effect lasts for 3 ticks. 
- The trance will turn the enemy into an allied soldier, meaning they will inherit the health and damage of an allied soldier whilst in trance.
- If 3 ticks have passed and the tranced enemy (allied soldier) is still alive, they will transfer back to the same type of enemy with their original health and damage just before they were tranced.


## Armour
| Armour  | Price (gold) | Damage reduction               | Drop rate from slaying enemies |
| ------  | ------       | ------                         | ------                         |
| Armour  | 65           | 50%                            | 3%                             |
| Helmet  | 60           | 15%                            | 10%                            |
| Shield  | 30           | 0% (no block) OR 100% (block)  | 5%                             |
| TreeStump | N/A        | 50% higher block chance vs boss | 1%                             |
- When a character has a shield equipped, they have a 40% chance to block an enemy's attack
- If a character blocks, they still attack back on the same tick

## Enemies & Allies
- The battle and support radius for each enemy will be calculated with radii as a path-based distance. 
- The fight order will be:
    - The character hits the enemy first.
    - Any ally in the character party hits the enemy.
    - The enemy hits the character and other allies.
    - Support enemies deal damage to the character.
- The maximum amount of allies allowed is 6.
- Allies will deal deal to each enemy that the character is fighting.
- The ally will receive damage from each enemy that the character is fighting.
- Multiple enemies can go onto the same path tile.
- Vampire starts to get away from the campfire with distance from the coordinates of two entity less than 4;
- Doggie's stun ability activates every 2 attacks.
- Doggie's stun causes the character to stop attack for 1 turn.
- Doggie runs away from the character (always downpath) and have 1 in 4 chance doesn't move.
- Elan's healing radius of the NPCs is 2 blocks.
- Elan can't heal himself.
- Elan always goes upPath.

## Battles
- When the Character is in a battle, they will fight enemies in the order that they were encountered. 
- Battle and support radius is calculated 'by-the-path' which means if an entity has a radius of 3 tiles, it will be measured along the ordered path.

| Entity        | Damage (per hit)    | Hit speed (CM)  | Walk speed (CM) | Health | Experience gained for slaying | Gold gained for slaying | Battle radius | Support Radius | Chance for ability to activate |
| ------        | ------              | ------          | ------          | ------ | ------                        | ------                  | ------        | ------         | ------                         |
| Slug          | 3                   | 1               | 1               | 6      | 10                            | 5                       | 1             | 1              | N/A                            |
| Zombie        | 5                   | 0.5             | 0.5             | 10     | 20                            | 10                      | 2             | 3              | 20%                            |
| Vampire       | 6 (8-10 critical bite) | 1               | 1               | 20     | 50                            | 25                      | 2             | 3              | 30%                            |
| Allied Soldier| 1                   | 1               | 1               | 40     | N/A                           | N/A                     | N/A           | N/A            | N/A                            |
| Character     | 3                   | 1               | 1               | 100    | N/A                           | N/A                     | N/A           | N/A            | N/A                            |

CM = Character Movement

## Buildings
- The Tower’s damage is 3, the radius of the Tower is 5.
- Village will regains 25 health for character
- Trap’s will damage 5 health for an enemy.
- Campfire’s radius is 5.
- Spawners can't be placed 2 tiles after the hero's building.


