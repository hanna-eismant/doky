export const noop = () => {
};

export const getRowsCount = str =>
  str.split('\n').length;

export const clamp = (value, min, max) => {
  if (value < min) {
    return min;
  }

  if (value > max) {
    return max;
  }

  return value;
};
