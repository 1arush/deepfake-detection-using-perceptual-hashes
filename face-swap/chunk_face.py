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

reals = [
    "C:/Users/HP/Downloads/deepfake_images/face-swap/f2.jpg",
    "C:/Users/HP/Downloads/deepfake_images/face-swap/f1.jpg",
    "C:/Users/HP/Downloads/deepfake_images/face-swap/m2.jpg",
    "C:/Users/HP/Downloads/deepfake_images/face-swap/m1.jpg"
]

fakes = [
    "C:/Users/HP/Downloads/deepfake_images/face-swap/f_swap_1_2.jpg",
    "C:/Users/HP/Downloads/deepfake_images/face-swap/f_swap_2_1.jpg",
    "C:/Users/HP/Downloads/deepfake_images/face-swap/m_swap_1_2.jpg",
    "C:/Users/HP/Downloads/deepfake_images/face-swap/m_swap_2_1.jpg"
]

for path in fakes:
    name = os.path.basename(path)
    base, ext = os.path.splitext(name)
    result[base] = []

for p in l:
    for i in range(len(reals)):
        real = Image.open(reals[i])
        fake = Image.open(fakes[i])

        dist, cnt = process_image_pair(fake, real, p)

        # separate the name
        name = os.path.basename(fakes[i])
        base, ext = os.path.splitext(name)
        result[base].append(dist)

print(result)

df = pd.DataFrame(result)
df.to_csv(f'py_faceswap.csv', index=False)
    