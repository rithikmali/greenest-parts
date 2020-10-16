import numpy as np
import cv2

no_of_imgs=80
result_file=open("result_file.txt","w")	
#image names should be consistent like image1.png or screenshot69.png
for i in range(1,no_of_imgs+1):
	image_name="image_part_0"
	if i<10:
		image_name+="0"
	i=str(i)
	image_name+= i+".jpg"
	# print(image_name)
	image = cv2.imread("/home/adeab/Documents/opencv/images/"+image_name) #images directory

	boundaries = ([4, 30, 4], [120, 255, 120]) #bgr boundaries for green

	lower = np.array(boundaries[0], dtype = "uint8") #this is unsigned integer of 8 bits I think
	upper = np.array(boundaries[1], dtype = "uint8")

	mask = cv2.inRange(image, lower, upper)
	output = cv2.bitwise_and(image, image, mask = mask)

	ratio_green=cv2.countNonZero(mask)/(image.size/3)
	percentage_green=str(np.round(ratio_green*100, 2))
	# print('green pixel percentage:', np.round(ratio_green*100, 2))
	result_file.write(image_name+" "+ "region"+i+" "+percentage_green+"\n")


result_file.close()


# cv2.imshow("images", np.hstack([image, output]))
# cv2.waitKey(0)


