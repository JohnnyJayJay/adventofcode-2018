import re
import numpy as np

inputs = open('../input.txt').read().strip().split('\n')

guard_asleep = {}
for line in sorted(inputs):
    minute = int(re.search(r':(\d+)', line).group(1))
    if 'wakes up' in line:
        if guard_id in guard_asleep.keys():
            guard_asleep[guard_id][last_minute:minute] += 1
        else:
            guard_asleep[guard_id] = np.zeros(60)
            guard_asleep[guard_id][last_minute:minute] += 1
    elif 'begins shift' in line:
        guard_id = int(re.search(r'#(\d+)', line).group(1))
    last_minute = minute

max_key = max(guard_asleep, key=lambda k: sum(guard_asleep[k]))
max_min = np.argmax(guard_asleep[max_key])
print(f'Solution one: {max_key * max_min}')

guard_asleep.clear()

for line in sorted(inputs):
    minute = int(re.search(r':(\d+)', line).group(1))
    if 'wakes up' in line:
        if guard_id in guard_asleep.keys():
            guard_asleep[guard_id][last_minute:minute] += 1
        else:
            guard_asleep[guard_id] = np.zeros(60)
            guard_asleep[guard_id][last_minute:minute] += 1
    elif 'begins shift' in line:
        guard_id = int(re.search(r'#(\d+)', line).group(1))
    last_minute = minute

max_key = max(guard_asleep, key=lambda k: max(guard_asleep[k]))
max_min = np.argmax(guard_asleep[max_key])
print(f'Solution two: {max_key * max_min}')
