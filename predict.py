import numpy as np
import json
from keras.models import load_model
import sys

i = np.array([json.loads(sys.argv[2])])
model = load_model(sys.argv[1])
o = model.predict(i)
print(o)