import React from 'react';
import {Button, SimpleSelect, TextInput} from "@instructure/ui";
interface ModalProps {
  isOpen: boolean;
  onClose: () => void;
  message: string;
}

const ConfirmModal: React.FC<ModalProps> = ({ isOpen, onClose, message }) => {
  if (!isOpen) return null;

  return (
    <div className="modal-overlay">
      <div className="modal">
      <Button
        className="sub-btn cancel-btn"
        themeOverride={{
        mediumPaddingTop: '5px',
        mediumPaddingBottom: '5px',
         }}
         onClick={onClose}
        >
         Cancel
        </Button>

        <Button
        className="sub-btn"
        color="primary"
        themeOverride={{
        mediumPaddingTop: '5px',
        mediumPaddingBottom: '5px'
        }}
        onClick={onClose}
    >
        Confirm
    </Button>
        <div className="modal-content">
          <p>{message}</p>
        </div>
      </div>
    </div>
  );
};

export default ConfirmModal;