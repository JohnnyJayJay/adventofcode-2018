import string
import math


inp = open('../input.txt').read()


def check_two_chars(a, b):
    return a.lower() == b.lower() and a != b


def part_one(data):
    stack = []
    for char in data:
        if len(stack) > 0 and check_two_chars(char, stack[-1]):
            stack.pop(-1)
        else:
            stack.append(char)
    return len(stack)


print(f'Remaining units:  {part_one(inp)}')


def part_two():
    shortest = -1
    for char in string.ascii_lowercase:
        data = [c for c in inp if c.lower() != char]
        shortest = part_one(data) if shortest == - \
            1 else min(shortest, part_one(data))
    return shortest


print(f'Shortest polymer: {part_two()}')
