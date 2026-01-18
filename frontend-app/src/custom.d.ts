// Keep only minimal ambient declarations to avoid shadowing real types from @types packages
declare const process: any;

declare namespace JSX {
  interface IntrinsicElements {
    [elemName: string]: any;
  }
}
