export const getApiBase = (): string => {
  if (typeof window !== "undefined") {
    if (window.location.port === "3000") {
      return "http://localhost:8000/api";
    }
    return `${window.location.origin}/api`;
  }
  return "http://localhost:8000/api";
};
