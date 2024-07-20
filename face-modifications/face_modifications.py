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

tot_path = "C:/Users/HP/Downloads/images/"

path_names = [ "m1", "m2", "f1", "f2" ]


# iterate over pathnames
for name in path_names:
    result = {}
    # now there are a bunch of folders
    # iterate over all except base
    for iter in ["eyes", "nose", "nose_code", "lips", "lips_code"]:
        li = []
        if iter.find("eyes")==-1:
            st = ""
            if iter.find("lips")!=-1:
                st="lip"
            else:
                st="nose"
            # we have to compare base and nosebox
            real_path = tot_path + name +'/'+ 'base' +'/base.jpg'
            fake_path = tot_path + name +'/'+ iter +'/base.jpg'
            real = Image.open(real_path)
            fake = Image.open(fake_path)
            d, cnt = process_image_pair(fake, real, 1)
            li.append(d)

            for p in range(1,6):
                real_path = tot_path + name +'/'+ 'base' +f'/{st}box.jpg'
                fake_path = tot_path + name +'/'+ iter +f'/{st}box.jpg'
                real = Image.open(real_path)
                fake = Image.open(fake_path)
                d, cnt = process_image_pair(fake, real, p)
                li.append(d)
        else:
            # we have to compare base and eyeboxes
            real_path = tot_path + name +'/'+ 'base' +'/base.jpg'
            fake_path = tot_path + name +'/'+ iter +'/base.jpg'
            real = Image.open(real_path)
            fake = Image.open(fake_path)
            d, cnt = process_image_pair(fake, real, 1)
            li.append(d)

            for p in range(1,6):
                x = 0
                for num in range(2):
                    real_path = tot_path + name +'/'+ 'base' +f'/eyebox_{num}.jpg'
                    fake_path = tot_path + name +'/'+ iter +f'/eyebox_{num}.jpg'
                    real = Image.open(real_path)
                    fake = Image.open(fake_path)
                    d, cnt = process_image_pair(fake, real, p)
                    if x<d:
                        x=d
                li.append(x)

        result[iter] = li
    df = pd.DataFrame(result)
    df.to_csv(f'{name}_face.csv', index=False)

    