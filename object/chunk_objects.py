from PIL import Image
import imagehash
import numpy as np
import pandas as pd
import math, os

SIZE = 8

def process_image_pair(fakeimg, realimg, p):
    width, height = fakeimg.size

    # Calculate the width and height of each subrectangle
    subwidth = math.ceil(width / p)
    subheight = math.ceil(height / p)

    ans, cnt = 0, 0

    for i in range(p):
        for j in range(p):
            # Calculate subrectangle boundaries
            left = j * subwidth
            upper = i * subheight
            right = min((j + 1) * subwidth, width)  # Ensure we don't exceed image width
            lower = min((i + 1) * subheight, height)  # Ensure we don't exceed image height

            # Crop subrectangles
            f_sub = fakeimg.crop((left, upper, right, lower))
            r_sub = realimg.crop((left, upper, right, lower))

            # Compute perceptual hashes using the SIZE constant
            f_hash = imagehash.phash(f_sub, hash_size=SIZE)
            r_hash = imagehash.phash(r_sub, hash_size=SIZE)

            # Compute hash difference
            dist = f_hash - r_hash
            if dist > ans:
                ans = dist
            cnt +=1

    return ans, cnt

l = [1,2,3,4,5,6]
result = {}
names = []
for i in range(1,9):
    names.append(f'object_{i}')
basepath = 'C:/Users/HP/Downloads/deepfake_images/object/'

for name in names:
    result[name] = []
    for p in l:
        fakeimg = Image.open(basepath + name + '_fake.jpg')
        realimg = Image.open(basepath + name + '.jpg')

        dist, cnt = process_image_pair(fakeimg, realimg, p)
        print(name, cnt)
        result[name].append(dist)


df = pd.DataFrame(result)
df.to_csv(f'py_obj.csv', index=False)
    