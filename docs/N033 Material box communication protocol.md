 # Get the color information of the Code file slice 

Subscribe to the virtual_sdcard incident. The extruder_colour field in the incident contains the color information of the Code file. Extruder_colour is represented by the JSON group. The color of the consumables is indicated by RGB. The color number of the group index corresponds to the color number. The format is as follows: 

```json 
[ 
    { 
        "R": 255, 
        "G": 0, 
        "B": 0 
    }, 
    { 
        "R": 0, 
        "G": 128, 
        "B": 0 
    }, 
    { 
        "R": 0, 
        "G": 0, 
        "B": 255 
    } 
] 
``` 

# Get information about the box 

Obtain material box information through webhooks'filament_hub/info` method 

request: 

```json 
{} 
``` 

response: 

```json 
{ 
    [ 
        { 
            "id": 0, // cartridge ID, main cartridge is 0, from cartridge is 1 
            "slots": 4, // number of cartridges 
            "model": "Anycubic AMS XXX", // model or hardware ID 
            "firmware": "v1.0.0" // solid version 
        }, 
        { 
            "id": 1, // cartridge ID, main cartridge is 0, and the feed box is 1 
            "slots": 4, // number of cartridges 
            "model": "Anycubic AMS XXX", // model or hardware ID 
            "firmware": "v1.0.0" // solid version 
        } 
    ] 
} 

``` 

# Get information on consumables 

Obtain consumable information through webhooks'filament_hub/filament_info` method 

request: 

```json 
{ 
    "id": 0, // cartridge ID, main cartridge is 0, from cartridge is 1 
    "index": 1 // material number ID 
} 
``` 

response: 

```json 
{ 
    "index": 1, // material number, starting from 0 
    "sku": "HPLGR-104", // SKU 
    "brand": "SUNLU", // brand 
    "type": "PLA", // Type of consumables PLA+ TPU ABS PETG 
    "source": 1 // Source of consumable information, 0-unknown, 1-RFID, 2-user definition, 3-no material 
    "color": [ // RGB color 
        255, // Red 
        255, // green 
        255 // Blue 
    ], 
    "extruder_temp": { // recommended temperature for extruder 
        "min": 200, // minimum temperature 
        "max": 210 // maximum temperature 
    }, 
    "hotbed_temp": { // hotbed recommended temperature 
        "min": 50, // minimum temperature 
        "max": 60 // maximum temperature 
    }, 
    "diameter": 1.75 // Consumable diameter, unit mm 
} 
``` 

# Feed box refund 

Overview: Withdraw the consumables of the designated material box from the extruder 

GCODE: 

```json 
UNWIND_FILAMENT ID=0 INDEX=1 
``` 

among them: 
* ID: Box number, main box is 0, from box is 1 
* INDEX: index of material number, value range 0~3 

Prerequisite: Zero, the current number in the squeeze out of the aircraft is consistent with the request. 

# Feed box feed 

Overview: Push the consumables of the designated material box into the extruder 

GCODE: 

```json 
FEED_FILAMENT ID=0 INDEX=1 
``` 

among them: 
* ID: Box number, main box is 0, from box is 1 
* INDEX: index of material number, value range 0~3 

Prerequisites: zeroing, squeezing out of the machine to 200 degrees Celsius, and currently out of the machine without consumables 

# Get the status of the material box 

Subscribe `filament_hub` message through webooks. 

Message format: 

```json 
{ 
    "auto_refill": 1, // Whether to turn on the automatic renewal, 0-close, 1-open 
    "ext_spool": 0, // Whether to use the peripheral disk, 0-Not used, 1-Use 
    "ext_spool_status": "ready", // outer platter status, ready-ready, runout-out 
    "current_filament": "0-3", // squeeze out the current material number of the machine, rule: "Material Box ID-Material number ID", if empty, the current squeeze out of the machine is not expected 
    "filament_hubs": [ 
        { 
            "id": 0, 
            "status": "ready", // cassette status 
            "dryer_status": { 
                "status": "drying", // dryer status: stop-stop drying, drying-drying-drying-drying, heater_err-heating failure 
                "target_temp": 60, // target temperature, unit: degree Celsius 
                "duaration": 120, // Long drying time, unit: minutes 
                "remain_time": 20 // remaining time, unit: minutes 
            }, 
            "temp": 50, // fox temperature, unit: degree Celsius 
            "humidity": 35, // Wet box humidity 
            "slots": [ // cassette tank information 
                { 
                    "index": 0, // Cassette Trough Index 
                    "status": "feeding", // cassette tank status 
                    "sku": "HPLGR-104", 
                    "type": "PLA", 
                    "color": [ // Consumable color: RGB 
                        255, 
                        255, 
                        0 
                    ], 
                    "source": 1 // Source of consumable information, 0-unknown, 1-RFID, 2-user definition 
                }, 
                { 
                    "index": 1, 
                    "status": "ready", 
                    "sku": "HPLGR-104", 
                    "type": "PLA", 
                    "color": [ 
                        255, 
                        255, 
                        0 
                    ], 
                    "source": 1 
                }, 
                { 
                    "index": 2, 
                    "status": "empty", 
                    "sku": "HPLGR-104", 
                    "type": "PLA", 
                    "color": [ 
                        255, 
                        255, 
                        0 
                    ], 
                    "source": 1 
                }, 
                { 
                    "index": 3, 
                    "status": "runout", 
                    "sku": "HPLGR-104", 
                    "type": "PLA", 
                    "color": [ 
                        255, 
                        255, 
                        0 
                    ], 
                    "source": 1 
                } 
            ] 
        }, 
        { 
            "id": 1, 
            "status": "ready", 
            "dryer_status": { 
                "status": "drying", 
                "target_temp": 60, 
                "duaration": 120, 
                "remaing_time": 20 
            }, 
            "temp": 50, 
            "humidity": 35, 
            "slots": [ 
                { 
                    "index": 0, 
                    "status": "ready", 
                    "sku": "HPLGR-104", 
                    "type": "PLA", 
                    "color": [ 
                        255, 
                        255, 
                        0 
                    ], 
                    "source": 1 
                }, 
                { 
                    "index": 1, 
                    "status": "empty", 
                    "sku": "HPLGR-104", 
                    "type": "PLA", 
                    "color": [ 
                        255, 
                        255, 
                        0 
                    ], 
                    "source": 1 
                }, 
                { 
                    "index": 2, 
                    "status": "empty", 
                    "sku": "HPLGR-104", 
                    "type": "PLA", 
                    "color": [ 
                        255, 
                        255, 
                        0 
                    ], 
                    "source": 1 
                }, 
                { 
                    "index": 3, 
                    "status": "runout", 
                    "sku": "HPLGR-104", 
                    "type": "PLA", 
                    "color": [ 
                        255, 
                        255, 
                        0 
                    ], 
                    "source": 1 
                } 
            ] 
        } 
    ] 
} 
``` 

Description: 

1. auto_refill: Has the automatic renewal function been turned on: 
  * 0: Close; 
  * 1: Open; 

2. ext_spool: Are you using an exterior tray: 
  * 0: Not used 
  * 1: Use 

3. ext_spool_status: The status of the exterior plate will only return to the field if it is ext_spool1 
  * ready: ready 
  * runout: broken 

4. current_filament: Squeeze out the current material number of the machine, coding rule: "Food Box ID-Food ID", if it is empty, the current squeeze out of the machine is not expected 

5. status: Current status of the material box, including: 

  * startup: the material box is starting; 
  * ready: The kit is activated and the upper machine order can be executed; 
  * busy: The material box is performing instructions, including feeding and refunding; 

6.  Drying status: 

* status: dryer status: stop-stop drying, drying-drying-drying, heater_err-heating failure 
* target_temp: target temperature, unit degrees Celsius 
* duration: long drying time, unit: minutes; 
* remain_time: remaining time, unit: minutes 

7. temp: fox temperature, unit of degrees Celsius 

8. humidity: humidity of the fox 

9. slots: Box slot information: 

* index: material tank index, starting from 0; 
* status: status of the material tank: 
    * feeding: feeding; 
    * unwinding-refunding; 
    * shifting-block changing; 
    * ready-the slot is ready; 
    * empty: no disk installed; 
    * runout: missing material tank; 
    * stuck: stuffing or blocking; 

* sku: material number information; 
* type: type of consumables; 
* color: RGB color; 
* source: Source of consumables information: 
    * 0-Unknown; 
    * 1-RFID; 
    * 2-User Editing 

# Turn on drying function 

Turn on the drying function through the `filament_hub/start_drying` method of webhooks 

request: 

```json 
{ 
    "id": 0, // cartridge ID, main cartridge is 0, from cartridge is 1 
    "fan_speed": 50.0, // fan turn number rpm 
    "duration": 60 // long drying time, unit minutes 
} 
``` 

response: 

```json 
{} 
``` 

# Stop drying function 

Stop drying function through webhooks'filament_hub/stop_drying` method 

request: 

```json 
{ 
    "id": 0 // cartridge ID, main cartridge is 0, and the feed box is 1 
} 
``` 

response: 

```json 
{} 
``` 


# Set dry fan turn 

Set the dry fan turn number information through webooks'filament_hub/set_fan_speed` method 

request: 

```json 
{ 
    "id": 0, // cartridge ID, main cartridge is 0, from cartridge is 1 
    "fan_speed": 7000 
} 
``` 

response: 

```json 
{} 
``` 

# Feed box configuration 

The material box configuration passes the `filament_hub/set_config` method of webhooks 

request: 

```json 
{ 
    "auto_refill": 1, // Whether to turn on the automatic renewal, 0-not open, 1-open 
    "ext_spool": 0, // Whether to use the peripheral disk, 0-Not applicable, 1-Use 
    "runout_detect": 1, // Whether to use the fracture test, 0-Not available, 1-Enable 
    "flush_multiplier": 1.0, // Washing coefficient, taking value range [0.0, 3.0] 
} 
``` 

response: 

```json 
{} 
``` 

# Get the configuration box configuration 

Get the material box configuration through webhooks'filament_hub/get_config` method 

request: 

```json 
{} 
``` 

response: 

```json 
{ 
    "auto_refill": 1, // Whether to turn on the automatic renewal, 0-not open, 1-open 
    "ext_spool": 0, // Whether to use the peripheral disk, 0-Not applicable, 1-Use 
    "runout_detect": 1, // Whether to use the fracture test, 0-Not available, 1-Enable 
    "flush_multiplier": 1.0, // Washing coefficient, taking value range [0.0, 3.0] 
} 
``` 


# Brush the color of the freshen box-remove the editing color 

The color of the refresher box passes the `filament_hub/filament_recognition` method of webhooks 

request: 

```json 
{ 
    "id": 0, // cartridge ID, main cartridge is 0, from cartridge is 1 
    "index": 1 // material number ID 
} 
``` 

response: 

```json 
{ 
} 
``` 


<a href=https://github.com/ANYCUBIC-3D/Kobra3/blob/91d9771204b00242668a02586cf317a58a1aa26f/klipper-go/docs/N033%E6%96%99%E7%9B%92%E9%80%9A%E4%BF%A1%E5%8D%8F%E8%AE%AE.md>Original File</a>