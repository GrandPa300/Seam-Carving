## Seam Carving Summary:

### Introduction
* Seam-carving is a content-aware image resizing technique where the image is reduced in size by one pixel of height (or width) at a time. (animation picture cited from [here](http://community.wolfram.com/groups/-/m/t/960843))

<div align=center>
	<img src="http://i.imgur.com/X79L7Ud.gif" width="40%" height="40%" />
</div>

* A vertical/horizontal seam in an image is a path of pixels connected from the top/left to the bottom/right with one pixel in each row/column. 
* Below left is the original  1600 x 1000 pixel image; below right is the result after removing about 30% in vertical and horizontal, resulting in a 50% smaller image. 

<div align=center>
	<img src="http://i.imgur.com/CkoXSu2.jpg" width="40%" height="40%" />
	<img src="http://i.imgur.com/yvaPypC.jpg" width="40%" height="40%" />
</div>

<div align=center>
	<img src="http://i.imgur.com/yhXf5eH.jpg" width="40%" height="40%" />
	<img src="http://i.imgur.com/4rrkKug.jpg" width="40%" height="40%" />
</div>

<div align=center>
	<img src="http://i.imgur.com/NPyrWJ5.jpg" width="40%" height="40%" />
	<img src="http://i.imgur.com/JNBqmAE.jpg" width="40%" height="40%" />	
</div>

* Unlike cropping and scaling, mean features of the image are preserved and "squeezed" into a smaller area.
* More about seam carving can be found at [here](https://en.wikipedia.org/wiki/Seam_carving).

### Implementation

* __Energy__:  energy of edge pixels are set to be 1000, and energy of all other pixels in the image are calculated based by _dual-gradient energy function_.

* __Lowest Energy Seam__: 
	* Seams are calculated by dynamic programming in this project: 
		 1) DP Status: lowest cumulative energy from top to current pixel are recorded in corresponding index of DP array.  
		 2) Initialization: pixels on 4 edges doesn't matter; all other pixels have initial energy + INFINITY
		 3) Status Transfer Function: new energy = minimum (energy from previous calculation, energy from current calculation)
	* For each pixel, energy of left, right and itself  are calculated; it's previous pixel are also recorded. 
	* After all the lowest cumulative energy are calculated, tracing back from last second row of image, start from lowest cumulative energy in this row, and track all the way back to top, and the result is the lowest energy seam. 

<div align=center>
	<div><img src="http://i.imgur.com/N0TTzUA.png"width = "30%" height = "30%"/></div>
	<div><img src="http://i.imgur.com/9KHZAIy.png"width = "30%" height = "30%"/></div>
	<div><img src="http://i.imgur.com/watMxrR.png"width = "30%" height = "30%"/></div>
</div>

* As shown below from left to right: _Original image, Horizontal Lowest Energy Seam, Vertical Lowest Energy Seam_

<div align=center>
	<img src="http://i.imgur.com/NPyrWJ5.jpg" width="30%" height="30%" />
	<img src="http://i.imgur.com/4R35sMs.jpg" width="30%" height="30%" />	
	<img src="http://i.imgur.com/P9FYWWD.jpg" width="30%" height="30%" />	
</div>

* This project is based on algs4, please check [Instruction](http://coursera.cs.princeton.edu/algs4/assignments/seamCarving.html) and [Check List](http://coursera.cs.princeton.edu/algs4/checklists/seamCarving.html) for more details. 
### Optimization
*  Instead re-initialize (re-calculate) all the energy after each seam carving, just recalculate the energy along the seam. (i.e. left & right pixel of seam pixel)
*  Added `Transpose` function, so for carving in horizontal, just need to transpose, find and carve seam in vertical, and transpose back. 

### Possible Future Improvements
1) Seam extending function to expand image.
2) Object removal by adding -INFINITY energy mask; Object protection by adding +INFINITY energy mask.