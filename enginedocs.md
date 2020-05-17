
# Escape Engine version 1.1.1b

## Preamble
The Escape Engine is a heavily modified and reworked version of the Infinity Engine, focusing primarily on static escape-the-room style games rather than infinite procedurally-generated worlds. The Escape Engine does not include some of the features of the Infinity Engine, such as: Tiles, Chunks, Batch Rendering, or Perlin Noise. It does, however, have features that are not yet present in the mainline version of the Infinity Engine such as UI Elements, constant mouse monitoring, and URL sprite loading.

## The Window Class
The Window class handles various things in the engine:

 - Creating the window
 - Drawing objects and graphics to the window
 - Drawing and processing UI elements
 - Managing the camera

### Creating a new Window
When creating a new instance of the Window class, you must provide the width of the window (as an integer), the height of the window (as an integer), and the name of the window (as a String).

### Introduction to the Camera
The Camera allows you to draw objects to the window relative to the camera's position. It has the default position of `(0, 0)` in the world. The camera's position is anchored to the top left of the screen (Pixel `(0, 0)` in Java's Graphics2D).

### Enabling and Disabling the Camera
The camera is enabled by default. If you wish to disable the camera, you may do so by setting the `enableCamera` variable to `false` in the instance of Window you wish to disable the camera on.

### Changing the Camera's position
##### Setting the X and Y position directly
The X and Y position of the camera can be set directly by changing the `cameraX` and `cameraY` variables in the instance of Window you wish to set the Camera's position on.

##### Centering the Camera on a point
If you wish to have a specific point in the center of the Camera's view, rather than at the top left, you can call the `centerCameraOn` function by passing an X position (as an integer) and a Y position (as an integer).

##### Centering the Camera on an object
If you wish to have a specific object in the center of the Camera's view, rather than at the top left, you can call the `centerCameraOn` function by passing a `WorldItem`-derived object. This function will automatically factor in the size of the object, and put the center of the object in the center of the Camera's view. This function does not modify the object's position in the world.

### Drawing Objects
Objects can be drawn to the screen by calling `drawWorldItem` and passing a `WorldItem`-derived node. It will automatically be drawn to screen, factoring in the Camera's position if the Camera is enabled.

### Drawing Text
Text can be drawn either centered on a position (using `drawTextCentered`), or using the position as the top-left anchor point (using `drawText`).  Both functions take the same arguments. To call either function, pass the X position (as an integer), the Y position (as an integer), the text to draw (as a String), the font size (as an integer), and the font color (as a `Color` object). Please note that these will not factor in the camera.

### Drawing and using Buttons
Buttons can be drawn to screen by passing the X position of the top-left corner (as an integer), the Y position of the top-left corner (as an integer), the desired width (as an integer), the desired height (as an integer), the desired text (as a String), the desired text color (as a `Color` object), the desired background color (as a `Color` object), and the desired background color on hover (as a `Color` object) to the `drawMenuButton` function. This function will return `true` if the button is being clicked and `false` if the button is not being clicked. As with all other graphics, the button should be redrawn each frame, so detecting button presses in this manner reduces the amount of code required. Please note this will not factor in the camera.

### Updating the screen
Whenever you wish to update the screen, you simply need to call the `repaint` function. Please note that this will clear the framebuffer.

### Drawing to the window as a Graphics2D object
The window's graphics are stored in a `BufferedImage` object named `frameBuffer`. You can directly modify its contents by calling `frameBuffer.createGraphics()` and running Graphics2D operations on the returned object. Please note that this will not factor in the camera.

### Getting mouse information
The variables `mouseX`, `mouseY`, and `mouseDown` will be constantly updated so long as the mouse is in the Window object you are referencing them from.

## The Keyboard Class
The Keyboard class is intended to be modified to add and remove keybindings and key variable definitions. To add a keybinding, add a boolean value to the top-level of the Keyboard class in `Keyboard.java` as well as an integer value. The boolean value is used to tell whether or not a key is pressed, and the integer value stores the `KeyEvent` scan code. Once those variables have been added, add a line to the `keyPressed` and `keyReleased` functions in the Keyboard class enabling and disabling the key's boolean value when `keycode` is equal to the integer value.

## The WorldItem Class
The WorldItem class is useful for decorative items that do not need to interact with the world. It supports loading its sprite from a file, a URL, an in-memory `BufferedImage	` object, or generating a solid color sprite of a given width and height.

##### Loading a sprite from a file on disk
When creating the WorldItem object, you can load a sprite from disk by passing the filename of the sprite (as a String) and the boolean value `false`. The Escape Engine will automatically attempt to load the file into memory. If it can't load the file, it will instead be replaced with a 64x64 tile of the colors #FF00FF, and #000000.

##### Loading a sprite from a URL
When creating the WorldItem object, you can load a sprite from a URL by passing the filename of the sprite (as a String) and the boolean value `true`. The Escape Engine will automatically attempt to load the file into memory. If it can't load the file, it will instead be replaced with a 64x64 tile of the colors #FF00FF, and #000000.

##### Generating a sprite of a solid color
To create a sprite with a particular color, pass the desired width (as an integer), height (as an integer), and color (as a `Color` object). A BufferedImage of the given width and height will be automatically created and filled with the color.

##### X and Y position
The WorldItem's position in the world can be viewed or changed by accessing the `x` and `y` variables of the instance.

## The CollisionItem Class
The CollisionItem is an extension of WorldItem that provides collision functions. To check for a collision with another CollisionItem, pass the other CollisionItem to the `collidingWith` function. To move in a direction while preventing collisions with another CollisionItem, pass the desired movement and the other CollisionItem to `moveAndCollide`. If you wish to move and collide with a group of other CollisionItems (which will be most cases), pass the desired movement and an array with all of the CollisionItems to `moveAndCollide`.

## Common Issues
The Escape Engine does not have a built-in way of telling the time since the last frame, so adding an FPS limiter is usually needed.

## Changelog
### Version 1.1.1b:
- Removed unused code and classes left in from v1.0b
### Version 1.1b:

 - Added collision functions from Infinity Engine

### Version 1.0b:

 - Added documentation
 - Game can now be prototyped

## To-do

 - Animation System
 - Delta Timer


