export type NavigationBarProps = {
  title: string;
  onCancel: () => void;
  onComplete: () => void;
};

export type InputFieldProps = {
  modelValue: string;
  placeholder: string;
};
