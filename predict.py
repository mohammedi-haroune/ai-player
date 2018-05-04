import numpy as np
import json
from keras.models import load_model
import sys
i=np.array([json.loads(sys.argv[1])])
model=load_model('model.h5')
o=model.predict(i)
print(o)